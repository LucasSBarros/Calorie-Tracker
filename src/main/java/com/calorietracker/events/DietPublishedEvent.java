package com.calorietracker.events;

import java.util.UUID;

public record DietPublishedEvent(
                UUID dietId,
                UUID userId,
                String userEmail,
                String userName,
                String dietName) {
}