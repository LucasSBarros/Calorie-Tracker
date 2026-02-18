package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UserRequestDto(
                @NotBlank String name,
                @NotNull @PositiveOrZero BigDecimal weight,
                @NotNull @PositiveOrZero BigDecimal height,
                @NotNull @PositiveOrZero Long age) {
}