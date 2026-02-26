package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.calorietracker.Calorie_Tracker.models.Gender;

public record UserDto(
        UUID idUser,
        String name,
        BigDecimal weight,
        BigDecimal height,
        Long age,
        Gender gender,
        BigDecimal imc,
        BigDecimal tmb) {
}