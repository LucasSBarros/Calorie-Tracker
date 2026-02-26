package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MealIngredientRequestDto(
                @NotNull @PositiveOrZero BigDecimal weight,
                UUID ingredientId) {
}