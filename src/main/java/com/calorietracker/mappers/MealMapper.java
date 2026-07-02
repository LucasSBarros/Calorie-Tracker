package com.calorietracker.mappers;

import java.util.LinkedHashSet;
import java.util.UUID;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.calorietracker.dtos.request.MealRequest;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.dtos.response.MealResponse;
import com.calorietracker.dtos.request.MealIngredientRequest;
import com.calorietracker.models.DietModel;
import com.calorietracker.models.MealIngredientModel;
import com.calorietracker.models.MealModel;
import com.calorietracker.repositories.DietRepository;

@Mapper(componentModel = "spring", uses = MealIngredientMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class MealMapper {

    @Autowired
    protected DietRepository dietRepository;

    @Autowired
    protected MealIngredientMapper mealIngredientMapper;

    public abstract MealResponse toResponse(MealModel model);

    @Mapping(target = "idMeal", ignore = true)
    @Mapping(target = "diet", source = "dietId")
    @Mapping(target = "totalCaloriesPerMeal", ignore = true)
    public abstract MealModel toEntity(MealRequest dto);

    @Mapping(target = "idMeal", ignore = true)
    @Mapping(target = "diet", source = "dietId")
    @Mapping(target = "mealIngredients", ignore = true)
    @Mapping(target = "totalCaloriesPerMeal", ignore = true)
    public abstract void updateEntityFromRequest(MealRequest dto, @MappingTarget MealModel entity);

    protected DietModel map(UUID dietId) {

        DietModel result = null;

        if (dietId != null) {
            result = dietRepository.findById(dietId)
                    .orElseThrow(() -> new ResourceNotFoundException("Diet not found: " + dietId));
        }

        return result;
    }

    @AfterMapping
    protected void afterCreate(MealRequest dto, @MappingTarget MealModel meal) {

        if (meal.getMealIngredients() == null) {
            meal.setMealIngredients(new LinkedHashSet<>());
        }

        for (MealIngredientModel mealIngredient : meal.getMealIngredients()) {
            mealIngredient.setMeal(meal);
        }

        meal.setTotalCaloriesPerMeal(meal.calculateTotalCaloriesPerMeal());
    }

    @AfterMapping
    protected void afterUpdate(MealRequest dto, @MappingTarget MealModel meal) {
        if (dto.mealIngredients() != null) {
            if (meal.getMealIngredients() == null) {
                meal.setMealIngredients(new LinkedHashSet<>());
            } else {
                meal.getMealIngredients().clear();
            }

            for (MealIngredientRequest mealIngredientRequest : dto.mealIngredients()) {
                var mealIngredient = mealIngredientMapper.toEntity(mealIngredientRequest);
                mealIngredient.setMeal(meal);
                meal.getMealIngredients().add(mealIngredient);
            }
        }

        meal.setTotalCaloriesPerMeal(meal.calculateTotalCaloriesPerMeal());
    }
}
