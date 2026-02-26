package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.GoalDto;
import com.calorietracker.dtos.GoalRequestDto;
import com.calorietracker.models.GoalModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GoalMapper {

    GoalDto toDto(GoalModel model);

    @Mapping(target = "idGoal", ignore = true)
    GoalModel toEntity(GoalRequestDto dto);

    @Mapping(target = "idGoal", ignore = true)
    void updateEntityFromDto(GoalRequestDto dto, @MappingTarget GoalModel entity);
}