package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StatusDto(
        UUID idStatus,
        UUID userId,
        BigDecimal weight,
        BigDecimal bf,
        LocalDateTime createdAt) {
}