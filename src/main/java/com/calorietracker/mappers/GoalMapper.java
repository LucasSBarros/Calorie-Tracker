package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.response.GoalResponse;
import com.calorietracker.dtos.request.GoalRequest;
import com.calorietracker.models.GoalModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GoalMapper {

    @Mapping(target = "userId", source = "user.idUser")
    GoalResponse toResponse(GoalModel model);

    @Mapping(target = "idGoal", ignore = true)
    @Mapping(target = "user", ignore = true)
    GoalModel toEntity(GoalRequest dto);

    @Mapping(target = "idGoal", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "startWeight", ignore = true)
    @Mapping(target = "startBf", ignore = true)
    void updateEntityFromRequest(GoalRequest dto, @MappingTarget GoalModel entity);
}