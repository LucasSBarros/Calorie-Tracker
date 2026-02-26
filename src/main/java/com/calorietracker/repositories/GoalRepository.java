package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.GoalModel;

import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<GoalModel, UUID> {

}
