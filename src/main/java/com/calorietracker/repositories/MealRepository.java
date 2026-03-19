package com.calorietracker.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.MealModel;

@Repository
public interface MealRepository extends JpaRepository<MealModel, UUID> {

        @EntityGraph(attributePaths = {
                        "mealIngredients",
                        "mealIngredients.ingredient",
                        "mealIngredients.ingredient.macro"
        })
        Optional<MealModel> findWithDetailsByIdMeal(UUID id);

        @Override
        @EntityGraph(attributePaths = {
                        "mealIngredients",
                        "mealIngredients.ingredient",
                        "mealIngredients.ingredient.macro"
        })
        List<MealModel> findAll();
}