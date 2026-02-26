package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;

import com.calorietracker.Calorie_Tracker.models.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UserRequestDto(
        @NotBlank String name,
        @NotNull @Positive BigDecimal weight,
        @NotNull @Positive BigDecimal height,
        @NotNull @PositiveOrZero Long age,
        @NotNull Gender gender) {
}