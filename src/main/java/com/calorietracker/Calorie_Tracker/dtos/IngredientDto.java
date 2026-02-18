package com.calorietracker.Calorie_Tracker.dtos;

import java.util.UUID;

public record IngredientDto(
                UUID idIngredient,
                String name,
                MacroDto macro) {
}