package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.response.StatusResponse;
import com.calorietracker.dtos.request.StatusRequest;
import com.calorietracker.models.StatusModel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatusMapper {

    @Mapping(target = "userId", source = "user.idUser")
    StatusResponse toResponse(StatusModel model);

    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    StatusModel toEntity(StatusRequest dto);

    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(StatusRequest dto, @MappingTarget StatusModel entity);
}