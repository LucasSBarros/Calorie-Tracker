package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record MealIngredientDto(
                UUID idMealIngredient,
                BigDecimal weight,
                IngredientDto ingredient) {
}