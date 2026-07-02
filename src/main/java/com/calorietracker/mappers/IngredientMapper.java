package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.response.IngredientResponse;
import com.calorietracker.dtos.request.IngredientRequest;
import com.calorietracker.models.IngredientModel;

@Mapper(componentModel = "spring", uses = MacroMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IngredientMapper {

    IngredientResponse toResponse(IngredientModel model);

    @Mapping(target = "idIngredient", ignore = true)
    IngredientModel toEntity(IngredientRequest dto);

    @Mapping(target = "idIngredient", ignore = true)
    void updateEntityFromRequest(IngredientRequest dto, @MappingTarget IngredientModel entity);
}