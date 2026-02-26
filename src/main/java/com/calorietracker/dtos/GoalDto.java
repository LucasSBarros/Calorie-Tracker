package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record GoalDto(
        UUID idGoal,
        BigDecimal weight,
        BigDecimal bf) {
}