package com.calorietracker.dtos.summary;

import java.math.BigDecimal;
import java.util.UUID;

public record MealIngredientSummaryResponse(
        UUID idMealIngredient,
        BigDecimal weight,
        String ingredientName,
        BigDecimal calories) {
}
