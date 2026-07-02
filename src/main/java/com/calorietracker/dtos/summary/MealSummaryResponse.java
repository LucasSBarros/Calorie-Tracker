package com.calorietracker.dtos.summary;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MealSummaryResponse(
        UUID idMeal,
        LocalDateTime mealDateTime,
        String description,
        BigDecimal totalCaloriesPerMeal,
        List<MealIngredientSummaryResponse> mealIngredients) {
}
