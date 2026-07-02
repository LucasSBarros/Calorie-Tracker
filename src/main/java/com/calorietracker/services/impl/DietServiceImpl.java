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

            dietMapper.updateEntityFromRequest(request, existing);
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
}
