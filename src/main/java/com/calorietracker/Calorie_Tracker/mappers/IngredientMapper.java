package com.calorietracker.Calorie_Tracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.Calorie_Tracker.dtos.IngredientDto;
import com.calorietracker.Calorie_Tracker.dtos.IngredientRequestDto;
import com.calorietracker.Calorie_Tracker.models.IngredientModel;

@Mapper(componentModel = "spring", uses = MacroMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IngredientMapper {

    IngredientDto toDto(IngredientModel model);

    @Mapping(target = "idIngredient", ignore = true)
    IngredientModel toEntity(IngredientRequestDto dto);

    @Mapping(target = "idIngredient", ignore = true)
    void updateEntityFromDto(IngredientRequestDto dto, @MappingTarget IngredientModel entity);
}