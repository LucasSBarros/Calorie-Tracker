package com.calorietracker.Calorie_Tracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.Calorie_Tracker.dtos.UserDto;
import com.calorietracker.Calorie_Tracker.dtos.UserRequestDto;
import com.calorietracker.Calorie_Tracker.models.UserModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto toDto(UserModel model);

    @Mapping(target = "idUser", ignore = true)
    UserModel toEntity(UserRequestDto dto);

    @Mapping(target = "idUser", ignore = true)
    void updateEntityFromDto(UserRequestDto dto, @MappingTarget UserModel entity);
}