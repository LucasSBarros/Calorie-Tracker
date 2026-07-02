package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.response.MealResponse;
import com.calorietracker.dtos.request.MealRequest;
import com.calorietracker.services.MealService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    /**
     * POST - /api/meals, Rota de criação de uma meala.
     * 
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<MealResponse> saveMeal(@RequestBody @Valid MealRequest request) {
        var created = mealService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/meals, Rota que busca por todas as mealas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MealResponse>> getAllMeals() {
        return ResponseEntity.status(HttpStatus.OK).body(mealService.findAll());
    }

    /**
     * GET - /api/meals/id, Rota que busca uma meala a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> getOneMeal(@PathVariable UUID id) {
        return mealService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/meals/id, Rota que atualiza uma meala
     * 
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<MealResponse> updateMeal(
            @PathVariable UUID id,
            @RequestBody @Valid MealRequest request) {

        return mealService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE - /api/meals/id, Rota que deleta uma meala
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable UUID id) {
        mealService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
