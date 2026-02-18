package com.calorietracker.Calorie_Tracker.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MealRequestDto(
                @NotNull LocalDateTime mealDateTime,
                String description,
                Set<MealIngredientRequestDto> mealIngredients,
                UUID dietId) {
}