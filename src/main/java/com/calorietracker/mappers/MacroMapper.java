package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.response.MacroResponse;
import com.calorietracker.dtos.request.MacroRequest;
import com.calorietracker.models.MacroModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MacroMapper {

    MacroResponse toResponse(MacroModel model);

    @Mapping(target = "idMacro", ignore = true)
    MacroModel toEntity(MacroRequest dto);

    @Mapping(target = "idMacro", ignore = true)
    void updateEntityFromRequest(MacroRequest dto, @MappingTarget MacroModel entity);
}