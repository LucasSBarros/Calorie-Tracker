package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.MealDto;
import com.calorietracker.dtos.MealRequestDto;
import com.calorietracker.services.MealService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meals")
@Validated
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    /**
     * POST - /api/meals, Rota de criação de uma meala.
     * 
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<MealDto> saveMeal(@RequestBody @Valid MealRequestDto requestDto) {
        MealDto created = mealService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/meals, Rota que busca por todas as mealas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MealDto>> getAllMeals() {
        return ResponseEntity.status(HttpStatus.OK).body(mealService.findAll());
    }

    /**
     * GET - /api/meals/id, Rota que busca uma meala a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<MealDto> getOneMeal(@PathVariable UUID id) {
        return mealService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/meals/id, Rota que atualiza uma meala
     * 
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<MealDto> updateMeal(
            @PathVariable UUID id,
            @RequestBody @Valid MealRequestDto requestDto) {

        return mealService.update(id, requestDto)
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
        boolean deleted = mealService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}