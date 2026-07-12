package com.calorietracker.dtos.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Representa um ingrediente e a quantidade consumida em uma refeição.
 *
 * @param ingredientId identificador do ingrediente
 * @param weight peso consumido em gramas
 */
public record MealLogItemRequest(
        @NotNull UUID ingredientId,
        @NotNull @Positive BigDecimal weight) {
}
