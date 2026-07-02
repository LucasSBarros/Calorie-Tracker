package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MealResponse(
        UUID idMeal,
        LocalDateTime mealDateTime,
        String description,
        List<MealIngredientResponse> mealIngredients,
        BigDecimal totalCaloriesPerMeal) {
}
