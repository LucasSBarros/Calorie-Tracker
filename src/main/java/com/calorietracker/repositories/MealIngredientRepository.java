package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.MealIngredientModel;

import java.util.UUID;

@Repository
public interface MealIngredientRepository extends JpaRepository<MealIngredientModel, UUID> {

}
