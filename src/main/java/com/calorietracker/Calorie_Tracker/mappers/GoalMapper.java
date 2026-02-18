package com.calorietracker.Calorie_Tracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.Calorie_Tracker.dtos.GoalDto;
import com.calorietracker.Calorie_Tracker.dtos.GoalRequestDto;
import com.calorietracker.Calorie_Tracker.models.GoalModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GoalMapper {

    GoalDto toDto(GoalModel model);

    @Mapping(target = "idGoal", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    GoalModel toEntity(GoalRequestDto dto);

    @Mapping(target = "idGoal", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    void updateEntityFromDto(GoalRequestDto dto, @MappingTarget GoalModel entity);
}