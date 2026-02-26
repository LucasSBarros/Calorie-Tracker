package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.MealDto;
import com.calorietracker.dtos.MealRequestDto;
import com.calorietracker.mappers.MealMapper;
import com.calorietracker.models.MealModel;
import com.calorietracker.repositories.MealRepository;
import com.calorietracker.services.MealService;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public MealServiceImpl(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    @Transactional
    public MealDto create(MealRequestDto request) {
        MealModel meal = mealMapper.toEntity(request);
        return mealMapper.toDto(mealRepository.save(meal));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealDto> findAll() {
        return mealRepository.findAll()
                .stream()
                .map(mealMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MealDto> findById(UUID id) {
        return mealRepository.findById(id).map(mealMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<MealDto> update(UUID id, MealRequestDto request) {
        return mealRepository.findById(id).map(existing -> {
            mealMapper.updateEntityFromDto(request, existing);
            MealModel saved = mealRepository.save(existing);
            return mealMapper.toDto(saved);
        });
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        mealRepository.deleteById(id);
        return true;
    }
}