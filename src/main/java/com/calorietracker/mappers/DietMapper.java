package com.calorietracker.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.calorietracker.dtos.DietDto;
import com.calorietracker.dtos.DietRequestDto;
import com.calorietracker.models.DietModel;

@Mapper(componentModel = "spring", uses = MealMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DietMapper {

    DietDto toDto(DietModel model);

    @Mapping(target = "idDiet", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "totalCalories", ignore = true)
    DietModel toEntity(DietRequestDto dto);

    @Mapping(target = "idDiet", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "totalCalories", ignore = true)
    void updateEntityFromDto(DietRequestDto dto, @MappingTarget DietModel entity);
}
