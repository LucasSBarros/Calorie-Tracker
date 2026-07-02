package com.calorietracker.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.calorietracker.dtos.response.MealIngredientResponse;
import com.calorietracker.dtos.request.MealIngredientRequest;
import com.calorietracker.exceptions.ResourceNotFoundException;
import com.calorietracker.models.IngredientModel;
import com.calorietracker.models.MealIngredientModel;
import com.calorietracker.repositories.IngredientRepository;

@Mapper(componentModel = "spring", uses = IngredientMapper.class)
public abstract class MealIngredientMapper {

    @Autowired
    protected IngredientRepository ingredientRepository;

    public abstract MealIngredientResponse toResponse(MealIngredientModel model);

    @Mapping(target = "idMealIngredient", ignore = true)
    @Mapping(target = "meal", ignore = true) // será setado no MealMapper @AfterMapping
    @Mapping(target = "ingredient", source = "ingredientId") // UUID -> IngredientModel
    public abstract MealIngredientModel toEntity(MealIngredientRequest dto);

    protected IngredientModel map(UUID ingredientId) {

        IngredientModel result = null;

        if (ingredientId != null) {
            result = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found: " + ingredientId));
        }

        return result;
    }
}