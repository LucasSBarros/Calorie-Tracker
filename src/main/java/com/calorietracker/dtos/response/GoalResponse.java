package com.calorietracker.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record GoalResponse(
        UUID idGoal,
        UUID userId,
        BigDecimal weight,
        BigDecimal startWeight,
        BigDecimal bf,
        BigDecimal startBf) {
}
