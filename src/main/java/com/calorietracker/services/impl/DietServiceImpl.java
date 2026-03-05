package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.DietDto;
import com.calorietracker.dtos.DietRequestDto;
import com.calorietracker.mappers.DietMapper;
import com.calorietracker.models.DietModel;
import com.calorietracker.repositories.DietRepository;
import com.calorietracker.services.DietService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final DietMapper dietMapper;

    @Override
    @Transactional
    public DietDto create(DietRequestDto request) {
        DietModel diet = dietMapper.toEntity(request);
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
    @Transactional
    public Optional<DietDto> update(UUID id, DietRequestDto request) {
        return dietRepository.findById(id).map(existing -> {
            dietMapper.updateEntityFromDto(request, existing);
            DietModel saved = dietRepository.save(existing);

            return dietRepository.findWithDetailsByIdDiet(saved.getIdDiet())
                    .map(dietMapper::toDto)
                    .orElseGet(() -> dietMapper.toDto(saved));
        });
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        if (!dietRepository.existsById(id))
            return false;
        dietRepository.deleteById(id);
        return true;
    }
}