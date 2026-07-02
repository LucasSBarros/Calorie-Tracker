package com.calorietracker.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record IngredientRequest(
        @NotBlank String name,
        MacroRequest macro) {
}
