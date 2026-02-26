package com.calorietracker.dtos;

import java.util.UUID;

public record IngredientDto(
                UUID idIngredient,
                String name,
                MacroDto macro) {
}