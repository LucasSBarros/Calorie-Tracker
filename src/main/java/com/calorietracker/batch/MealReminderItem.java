package com.calorietracker.batch;

import java.time.LocalDateTime;

import com.calorietracker.models.UserModel;

public record MealReminderItem(
                UserModel user,
                LocalDateTime sentAt,
                String type) {
}