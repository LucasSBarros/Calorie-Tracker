package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Apresenta uma refeição consumida e seus valores nutricionais registrados.
 *
 * @param id identificador do registro
 * @param consumedAt data e hora do consumo
 * @param description descrição da refeição
 * @param dietId identificador da dieta relacionada
 * @param totalCalories total de calorias consumidas
 * @param items itens que compõem a refeição
 */
public record MealLogResponse(
        UUID id,
        LocalDateTime consumedAt,
        String description,
        UUID dietId,
        BigDecimal totalCalories,
        List<MealLogItemResponse> items) {
}
