package com.calorietracker.dtos.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MealRequest(
        @NotNull LocalDateTime mealDateTime,
        String description,
        List<@Valid MealIngredientRequest> mealIngredients,
        UUID dietId) {
}
