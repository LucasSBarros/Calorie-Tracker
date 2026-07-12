package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Apresenta um item registrado em uma refeição consumida.
 *
 * @param id identificador do item
 * @param ingredientId identificador do ingrediente
 * @param ingredientName nome do ingrediente
 * @param weight peso consumido em gramas
 * @param calories calorias calculadas e preservadas no registro
 */
public record MealLogItemResponse(
        UUID id,
        UUID ingredientId,
        String ingredientName,
        BigDecimal weight,
        BigDecimal calories) {
}
