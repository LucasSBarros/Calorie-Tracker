package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.response.UserResponse;
import com.calorietracker.dtos.request.UserRequest;
import com.calorietracker.models.UserModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "age", expression = "java(model.calculateAge())")
    UserResponse toResponse(UserModel model);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    @Mapping(target = "diets", ignore = true)
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    @Mapping(target = "mealLogs", ignore = true)
    void updateEntityFromRequest(UserRequest dto, @MappingTarget UserModel entity);
}
