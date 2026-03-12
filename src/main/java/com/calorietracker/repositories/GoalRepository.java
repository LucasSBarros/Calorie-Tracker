package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.GoalModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<GoalModel, UUID> {

    Optional<GoalModel> findByUser_IdUser(UUID userId);

    boolean existsByUser_IdUser(UUID userId);
}
