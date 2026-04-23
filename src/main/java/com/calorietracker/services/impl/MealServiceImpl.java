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
import com.calorietracker.models.DietModel;
import com.calorietracker.models.MealModel;
import com.calorietracker.repositories.DietRepository;
import com.calorietracker.repositories.MealRepository;
import com.calorietracker.services.MealService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final DietRepository dietRepository;
    private final MealMapper mealMapper;

    @Override
    public MealDto create(MealRequestDto request) {

        MealModel meal = mealMapper.toEntity(request);
        MealModel saved = mealRepository.save(meal);
        updateDietTotalCalories(saved.getDiet());

        return mealMapper.toDto(saved);
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
            UUID oldDietId = existing.getDiet() != null
                    ? existing.getDiet().getIdDiet()
                    : null;
            mealMapper.updateEntityFromDto(request, existing);
            MealModel saved = mealRepository.save(existing);
            updateDietTotalCalories(saved.getDiet());

            if (oldDietId != null
                    && saved.getDiet() != null
                    && !oldDietId.equals(saved.getDiet().getIdDiet())) {

                DietModel oldDiet = dietRepository.findById(oldDietId)
                        .orElseThrow(() -> new ResourceNotFoundException("Diet not found: " + oldDietId));
                updateDietTotalCalories(oldDiet);

            }

            result = Optional.of(mealMapper.toDto(saved));

        }

        return result;

    }

    @Override
    public void delete(UUID id) {

        MealModel meal = mealRepository.findWithDetailsByIdMeal(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + id));
        DietModel diet = meal.getDiet();
        mealRepository.delete(meal);
        updateDietTotalCalories(diet);
    }

    /**
     * Atualiza o total de calorias da dieta com base nas refeições atualmente
     * cadastradas.
     * 
     * @param diet dieta que terá o total recalculado
     */

    private void updateDietTotalCalories(DietModel diet) {

        if (diet != null) {
            DietModel managedDiet = dietRepository.findWithDetailsByIdDiet(diet.getIdDiet())
                    .orElseThrow(() -> new ResourceNotFoundException("Diet not found: " + diet.getIdDiet()));
            managedDiet.updateTotalCalories();
            dietRepository.save(managedDiet);
        }
    }
}