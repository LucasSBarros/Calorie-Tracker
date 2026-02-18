package com.calorietracker.Calorie_Tracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record UserDto(
                UUID idUser,
                String name,
                BigDecimal weight,
                BigDecimal height,
                Long age) {
}