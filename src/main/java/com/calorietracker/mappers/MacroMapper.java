package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.MacroDto;
import com.calorietracker.dtos.MacroRequestDto;
import com.calorietracker.models.MacroModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MacroMapper {

    MacroDto toDto(MacroModel model);

    @Mapping(target = "idMacro", ignore = true)
    MacroModel toEntity(MacroRequestDto dto);

    @Mapping(target = "idMacro", ignore = true)
    void updateEntityFromDto(MacroRequestDto dto, @MappingTarget MacroModel entity);
}