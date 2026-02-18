package com.calorietracker.Calorie_Tracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.Calorie_Tracker.dtos.StatusDto;
import com.calorietracker.Calorie_Tracker.dtos.StatusRequestDto;
import com.calorietracker.Calorie_Tracker.models.StatusModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatusMapper {

    StatusDto toDto(StatusModel model);

    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    StatusModel toEntity(StatusRequestDto dto);

    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    void updateEntityFromDto(StatusRequestDto dto, @MappingTarget StatusModel entity);
}