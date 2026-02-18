package com.calorietracker.Calorie_Tracker.repositories;

import com.calorietracker.Calorie_Tracker.models.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<StatusModel, UUID> {

}
