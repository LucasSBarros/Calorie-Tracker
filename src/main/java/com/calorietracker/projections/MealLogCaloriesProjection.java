package com.calorietracker.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Projeta os dados mínimos de uma refeição usados nas agregações do relatório.
 *
 * @param consumedAt data e hora do consumo
 * @param calories total de calorias da refeição
 */
public record MealLogCaloriesProjection(
        LocalDateTime consumedAt,
        BigDecimal calories) {
}
