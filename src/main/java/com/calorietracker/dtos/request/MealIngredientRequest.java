package com.calorietracker.dtos.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MealIngredientRequest(
        @NotNull @PositiveOrZero BigDecimal weight,
        UUID ingredientId) {
}
