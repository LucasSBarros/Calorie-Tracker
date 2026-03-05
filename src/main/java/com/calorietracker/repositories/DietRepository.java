package com.calorietracker.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calorietracker.models.DietModel;

@Repository
public interface DietRepository extends JpaRepository<DietModel, UUID> {

    @EntityGraph(attributePaths = {
            "meals",
            "meals.mealIngredients",
            "meals.mealIngredients.ingredient",
            "meals.mealIngredients.ingredient.macro"
    })
    Optional<DietModel> findWithDetailsByIdDiet(UUID idDiet);

    @Override
    @EntityGraph(attributePaths = {
            "meals",
            "meals.mealIngredients",
            "meals.mealIngredients.ingredient",
            "meals.mealIngredients.ingredient.macro"
    })
    List<DietModel> findAll();
}