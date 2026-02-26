package com.calorietracker.dtos;

import jakarta.validation.constraints.NotBlank;

public record IngredientRequestDto(
        @NotBlank String name,
        MacroRequestDto macro) {
}