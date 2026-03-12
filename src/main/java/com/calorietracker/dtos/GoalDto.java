package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record GoalDto(
        UUID idGoal,
        UUID userId,
        BigDecimal weight,
        BigDecimal startWeight,
        BigDecimal bf,
        BigDecimal startBf) {
}