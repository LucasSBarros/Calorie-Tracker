package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.UUID;

public record MealDto(
        UUID idMeal,
        LocalDateTime mealDateTime,
        String description,
        LinkedHashSet<MealIngredientDto> mealIngredients,
        BigDecimal totalCaloriesPerMeal) {
}