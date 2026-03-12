package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.UserDto;
import com.calorietracker.dtos.UserRequestDto;
import com.calorietracker.models.UserModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "age", expression = "java(model.calculateAge())")
    UserDto toDto(UserModel model);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    UserModel toEntity(UserRequestDto dto);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "imc", ignore = true)
    @Mapping(target = "tmb", ignore = true)
    void updateEntityFromDto(UserRequestDto dto, @MappingTarget UserModel entity);
}