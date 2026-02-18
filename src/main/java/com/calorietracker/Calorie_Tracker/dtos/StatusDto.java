package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record StatusDto(
                UUID idStatus,
                BigDecimal weight,
                BigDecimal imc,
                BigDecimal bf,
                BigDecimal tmb) {
}