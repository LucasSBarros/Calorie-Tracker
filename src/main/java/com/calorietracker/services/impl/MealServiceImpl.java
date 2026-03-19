package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.MealDto;
import com.calorietracker.dtos.MealRequestDto;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.mappers.MealMapper;
import com.calorietracker.models.MealModel;
import com.calorietracker.repositories.MealRepository;
import com.calorietracker.services.MealService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Override
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
        return mealRepository.findWithDetailsByIdMeal(id).map(mealMapper::toDto);
    }

    @Override
    public Optional<MealDto> update(UUID id, MealRequestDto request) {

        Optional<MealDto> result = Optional.empty();

        Optional<MealModel> mealOpt = mealRepository.findWithDetailsByIdMeal(id);

        if (mealOpt.isPresent()) {

            MealModel existing = mealOpt.get();

            mealMapper.updateEntityFromDto(request, existing);

            MealModel saved = mealRepository.save(existing);

            result = Optional.of(mealMapper.toDto(saved));
        }

        return result;
    }

    @Override
    public void delete(UUID id) {
        if (!mealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meal not found: " + id);
        }
        mealRepository.deleteById(id);
    }
}