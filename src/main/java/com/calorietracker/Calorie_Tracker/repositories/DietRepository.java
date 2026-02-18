package com.calorietracker.Calorie_Tracker.repositories;

import com.calorietracker.Calorie_Tracker.models.DietModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DietRepository extends JpaRepository<DietModel, UUID> {

}
