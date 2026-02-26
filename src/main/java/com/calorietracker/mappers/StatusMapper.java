package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;
import com.calorietracker.models.StatusModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatusMapper {

    StatusDto toDto(StatusModel model);

    @Mapping(target = "idStatus", ignore = true)
    StatusModel toEntity(StatusRequestDto dto);

    @Mapping(target = "idStatus", ignore = true)
    void updateEntityFromDto(StatusRequestDto dto, @MappingTarget StatusModel entity);
}