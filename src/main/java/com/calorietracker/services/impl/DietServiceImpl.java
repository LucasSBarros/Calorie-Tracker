package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import com.calorietracker.events.DietPublishedEvent;

import com.calorietracker.dtos.response.DietResponse;
import com.calorietracker.dtos.request.DietRequest;
import com.calorietracker.exceptions.BusinessException;
import com.calorietracker.exceptions.ConflictException;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.mappers.DietMapper;
import com.calorietracker.models.DietModel;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.DietRepository;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.services.DietService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final UserRepository userRepository;
    private final DietMapper dietMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public DietResponse create(DietRequest request) {
        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        validatePeriod(request);
        validateNoOverlap(user.getIdUser(), null, request);

        var diet = dietMapper.toEntity(request);
        diet.setUser(user);
        diet.updateTotalCalories();

        var saved = dietRepository.save(diet);

        eventPublisher.publishEvent(
                new DietPublishedEvent(
                        saved.getIdDiet(),
                        user.getIdUser(),
                        user.getEmail(),
                        user.getName(),
                        saved.getName()));

        return dietRepository.findWithDetailsByIdDiet(saved.getIdDiet())
                .map(dietMapper::toResponse)
                .orElseGet(() -> dietMapper.toResponse(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietResponse> findAll() {
        return dietRepository.findAll()
                .stream()
                .map(dietMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DietResponse> findById(UUID id) {
        return dietRepository.findWithDetailsByIdDiet(id)
                .map(dietMapper::toResponse);
    }

    @Override
    public Optional<DietResponse> update(UUID id, DietRequest request) {

        Optional<DietResponse> result = Optional.empty();

        Optional<DietModel> dietOpt = dietRepository.findById(id);

        if (dietOpt.isPresent()) {
            var existing = dietOpt.get();

            var user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

            validatePeriod(request);
            validateNoOverlap(user.getIdUser(), existing.getIdDiet(), request);

            dietMapper.updateEntityFromRequest(request, existing);
            existing.setInitialDate(request.initialDate());
            existing.setFinalDate(request.finalDate());
            existing.setUser(user);

            var saved = dietRepository.save(existing);

            var response = dietRepository.findWithDetailsByIdDiet(saved.getIdDiet())
                    .map(dietMapper::toResponse)
                    .orElseGet(() -> dietMapper.toResponse(saved));

            result = Optional.of(response);
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        if (!dietRepository.existsById(id)) {
            throw new ResourceNotFoundException("Diet not found: " + id);
        }

        dietRepository.deleteById(id);
    }

    /**
     * Valida se a data inicial da dieta não ocorre depois da data final.
     *
     * @param request dados da dieta
     */
    private void validatePeriod(DietRequest request) {
        if (request.initialDate() != null
                && request.finalDate() != null
                && request.initialDate().isAfter(request.finalDate())) {
            throw new BusinessException("Diet initial date must be before or equal to final date");
        }
    }

    /**
     * Impede que um usuário possua dietas com períodos de vigência sobrepostos.
     *
     * @param userId identificador do usuário
     * @param excludedDietId dieta ignorada durante uma atualização ou nula
     * @param request período da dieta que será persistida
     */
    private void validateNoOverlap(UUID userId, UUID excludedDietId, DietRequest request) {
        long overlaps = dietRepository.countOverlappingPeriods(
                userId,
                excludedDietId,
                request.initialDate(),
                request.finalDate());

        if (overlaps > 0) {
            throw new ConflictException("The user already has a diet in the informed period");
        }
    }
}
