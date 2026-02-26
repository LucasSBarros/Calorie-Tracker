package com.calorietracker.Calorie_Tracker.dtos;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MealRequestDto(
                @NotNull LocalDateTime mealDateTime,
                String description,
                LinkedHashSet<MealIngredientRequestDto> mealIngredients,
                UUID dietId) {
}