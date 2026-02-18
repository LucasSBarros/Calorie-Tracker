package com.calorietracker.Calorie_Tracker.repositories;

import com.calorietracker.Calorie_Tracker.models.MacroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MacroRepository extends JpaRepository<MacroModel, UUID> {

}
