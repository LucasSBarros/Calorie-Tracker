package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record MealIngredientReportDto(
        UUID idMealIngredient,
        BigDecimal weight,
        String ingredientName,
        BigDecimal calories) {
}