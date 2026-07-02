package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MealIngredientResponse(
        UUID idMealIngredient,
        BigDecimal weight,
        IngredientResponse ingredient) {
}
