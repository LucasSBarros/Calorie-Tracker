package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.MealIngredientDto;
import com.calorietracker.dtos.MealIngredientRequestDto;
import com.calorietracker.models.MealIngredientModel;

@Mapper(componentModel = "spring", uses = IngredientMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MealIngredientMapper {

    MealIngredientDto toDto(MealIngredientModel model);

    @Mapping(target = "idMealIngredient", ignore = true)
    @Mapping(target = "meal", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    MealIngredientModel toEntity(MealIngredientRequestDto dto);

    @Mapping(target = "idMealIngredient", ignore = true)
    @Mapping(target = "meal", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    void updateEntityFromDto(MealIngredientRequestDto dto, @MappingTarget MealIngredientModel entity);
}