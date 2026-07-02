package com.calorietracker.dtos.response;

import java.util.UUID;

public record IngredientResponse(
        UUID idIngredient,
        String name,
        MacroResponse macro) {
}
