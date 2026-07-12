package com.calorietracker.dtos.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Contém os dados necessários para registrar uma refeição consumida.
 *
 * @param consumedAt data e hora em que a refeição foi consumida
 * @param description descrição opcional da refeição
 * @param dietId identificador opcional da dieta relacionada
 * @param items ingredientes e quantidades consumidas
 */
public record MealLogRequest(
        @NotNull LocalDateTime consumedAt,
        @Size(max = 255) String description,
        UUID dietId,
        @NotEmpty List<@Valid MealLogItemRequest> items) {
}
