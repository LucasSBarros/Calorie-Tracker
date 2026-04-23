package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MealReportDto(
        UUID idMeal,
        LocalDateTime mealDateTime,
        String description,
        BigDecimal totalCaloriesPerMeal,
        List<MealIngredientReportDto> mealIngredients) {
}