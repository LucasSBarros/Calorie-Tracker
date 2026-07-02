package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StatusResponse(
        UUID idStatus,
        UUID userId,
        BigDecimal weight,
        BigDecimal bf,
        LocalDateTime createdAt) {
}
