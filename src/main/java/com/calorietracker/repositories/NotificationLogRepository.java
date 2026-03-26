package com.calorietracker.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.NotificationLogModel;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLogModel, UUID> {

        boolean existsByUser_IdUserAndTypeAndSentAtAfter(UUID userId, String type, LocalDateTime sentAt);
}