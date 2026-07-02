package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.response.DietResponse;
import com.calorietracker.dtos.request.DietRequest;
import com.calorietracker.models.DietModel;

@Mapper(componentModel = "spring", uses = MealMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DietMapper {

    DietResponse toResponse(DietModel model);

    @Mapping(target = "idDiet", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "totalCalories", ignore = true)
    DietModel toEntity(DietRequest dto);

    @Mapping(target = "idDiet", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "totalCalories", ignore = true)
    void updateEntityFromRequest(DietRequest dto, @MappingTarget DietModel entity);
}
