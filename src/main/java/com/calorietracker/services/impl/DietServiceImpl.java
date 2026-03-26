package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.DietDto;
import com.calorietracker.dtos.DietRequestDto;
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

    @Override
    public DietDto create(DietRequestDto request) {
        UserModel user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        DietModel diet = dietMapper.toEntity(request);
        diet.setUser(user);

        DietModel saved = dietRepository.save(diet);

        return dietRepository.findWithDetailsByIdDiet(saved.getIdDiet())
                .map(dietMapper::toDto)
                .orElseGet(() -> dietMapper.toDto(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietDto> findAll() {
        return dietRepository.findAll()
                .stream()
                .map(dietMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DietDto> findById(UUID id) {
        return dietRepository.findWithDetailsByIdDiet(id)
                .map(dietMapper::toDto);
    }

    @Override
    public Optional<DietDto> update(UUID id, DietRequestDto request) {

        Optional<DietDto> result = Optional.empty();

        Optional<DietModel> dietOpt = dietRepository.findById(id);

        if (dietOpt.isPresent()) {
            DietModel existing = dietOpt.get();

            UserModel user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

            dietMapper.updateEntityFromDto(request, existing);
            existing.setUser(user);

            DietModel saved = dietRepository.save(existing);

            DietDto dto = dietRepository.findWithDetailsByIdDiet(saved.getIdDiet())
                    .map(dietMapper::toDto)
                    .orElseGet(() -> dietMapper.toDto(saved));

            result = Optional.of(dto);
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