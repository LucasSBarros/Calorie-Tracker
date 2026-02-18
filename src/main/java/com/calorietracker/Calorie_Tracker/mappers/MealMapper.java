package com.calorietracker.Calorie_Tracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.Calorie_Tracker.dtos.MealDto;
import com.calorietracker.Calorie_Tracker.dtos.MealRequestDto;
import com.calorietracker.Calorie_Tracker.models.MealModel;

@Mapper(componentModel = "spring", uses = MealIngredientMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MealMapper {

    MealDto toDto(MealModel model);

    @Mapping(target = "idMeal", ignore = true)
    @Mapping(target = "diet", ignore = true)
    @Mapping(target = "totalCaloriesPerMeal", ignore = true)
    MealModel toEntity(MealRequestDto dto);

    @Mapping(target = "idMeal", ignore = true)
    @Mapping(target = "diet", ignore = true)
    @Mapping(target = "totalCaloriesPerMeal", ignore = true)
    void updateEntityFromDto(MealRequestDto dto, @MappingTarget MealModel entity);
}