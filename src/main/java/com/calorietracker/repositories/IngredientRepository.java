package com.calorietracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.IngredientModel;

import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientModel, UUID> {

}
