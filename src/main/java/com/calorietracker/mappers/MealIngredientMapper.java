package com.calorietracker.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.calorietracker.dtos.MealIngredientDto;
import com.calorietracker.dtos.MealIngredientRequestDto;
import com.calorietracker.models.IngredientModel;
import com.calorietracker.models.MealIngredientModel;
import com.calorietracker.repositories.IngredientRepository;

@Mapper(componentModel = "spring", uses = IngredientMapper.class)
public abstract class MealIngredientMapper {

    @Autowired
    protected IngredientRepository ingredientRepository;

    public abstract MealIngredientDto toDto(MealIngredientModel model);

    @Mapping(target = "idMealIngredient", ignore = true)
    @Mapping(target = "meal", ignore = true) // será setado no MealMapper @AfterMapping
    @Mapping(target = "ingredient", source = "ingredientId") // UUID -> IngredientModel
    public abstract MealIngredientModel toEntity(MealIngredientRequestDto dto);

    protected IngredientModel map(UUID ingredientId) {
        if (ingredientId == null)
            return null;
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found: " + ingredientId));
    }
}