package com.calorietracker.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.response.MealResponse;
import com.calorietracker.dtos.request.MealRequest;
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
    public MealResponse create(MealRequest request) {

        var meal = mealMapper.toEntity(request);
        var saved = mealRepository.save(meal);
        updateDietTotalCalories(saved.getDiet());

        return mealMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealResponse> findAll() {
        return mealRepository.findAll()
                .stream()
                .map(mealMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MealResponse> findById(UUID id) {
        return mealRepository.findWithDetailsByIdMeal(id).map(mealMapper::toResponse);
    }

    @Override
    public Optional<MealResponse> update(UUID id, MealRequest request) {

        Optional<MealResponse> result = Optional.empty();
        Optional<MealModel> mealOpt = mealRepository.findWithDetailsByIdMeal(id);

        if (mealOpt.isPresent()) {

            var existing = mealOpt.get();
            var oldDietId = existing.getDiet() != null
                    ? existing.getDiet().getIdDiet()
                    : null;
            mealMapper.updateEntityFromRequest(request, existing);
            var saved = mealRepository.save(existing);
            updateDietTotalCalories(saved.getDiet());

            if (oldDietId != null
                    && saved.getDiet() != null
                    && !oldDietId.equals(saved.getDiet().getIdDiet())) {

                var oldDiet = dietRepository.findById(oldDietId)
                        .orElseThrow(() -> new ResourceNotFoundException("Diet not found: " + oldDietId));
                updateDietTotalCalories(oldDiet);

            }

            result = Optional.of(mealMapper.toResponse(saved));

        }

        return result;

    }

    @Override
    public void delete(UUID id) {

        var meal = mealRepository.findWithDetailsByIdMeal(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + id));
        var diet = meal.getDiet();
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
            var managedDiet = dietRepository.findWithDetailsByIdDiet(diet.getIdDiet())
                    .orElseThrow(() -> new ResourceNotFoundException("Diet not found: " + diet.getIdDiet()));
            managedDiet.updateTotalCalories();
            dietRepository.save(managedDiet);
        }
    }
}
