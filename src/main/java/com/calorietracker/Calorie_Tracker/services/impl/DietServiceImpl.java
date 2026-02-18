package com.calorietracker.Calorie_Tracker.services.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.Calorie_Tracker.dtos.DietDto;
import com.calorietracker.Calorie_Tracker.dtos.DietRequestDto;
import com.calorietracker.Calorie_Tracker.mappers.DietMapper;
import com.calorietracker.Calorie_Tracker.models.DietModel;
import com.calorietracker.Calorie_Tracker.repositories.DietRepository;
import com.calorietracker.Calorie_Tracker.services.DietService;

@Service
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final DietMapper dietMapper;

    public DietServiceImpl(DietRepository dietRepository, DietMapper dietMapper) {
        this.dietRepository = dietRepository;
        this.dietMapper = dietMapper;
    }

    @Override
    @Transactional
    public DietDto create(DietRequestDto request) {
        DietModel diet = dietMapper.toEntity(request);

        diet.setMeals(new HashSet<>());
        diet.setTotalCalories(BigDecimal.ZERO);

        DietModel saved = dietRepository.save(diet);
        return dietMapper.toDto(saved);
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
        return dietRepository.findById(id).map(dietMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<DietDto> update(UUID id, DietRequestDto request) {
        return dietRepository.findById(id).map(existing -> {
            dietMapper.updateEntityFromDto(request, existing);
            DietModel saved = dietRepository.save(existing);
            return dietMapper.toDto(saved);
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