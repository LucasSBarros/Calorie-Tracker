package com.calorietracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record StatusDto(
                UUID idStatus,
                BigDecimal weight,
                BigDecimal bf) {
}