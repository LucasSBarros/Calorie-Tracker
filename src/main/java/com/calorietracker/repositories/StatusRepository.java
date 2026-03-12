package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.StatusModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<StatusModel, UUID> {

    List<StatusModel> findByUser_IdUserOrderByCreatedAtDesc(UUID userId);

    Optional<StatusModel> findFirstByUser_IdUserOrderByCreatedAtDesc(UUID userId);

    List<StatusModel> findByUser_IdUserAndCreatedAtBetweenOrderByCreatedAtDesc(
            UUID userId,
            LocalDateTime start,
            LocalDateTime end);

}
