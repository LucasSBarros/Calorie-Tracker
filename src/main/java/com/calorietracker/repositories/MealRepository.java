package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.MealModel;

import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<MealModel, UUID> {

}
