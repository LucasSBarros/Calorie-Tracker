package com.calorietracker.Calorie_Tracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.Calorie_Tracker.dtos.DietDto;
import com.calorietracker.Calorie_Tracker.dtos.DietRequestDto;
import com.calorietracker.Calorie_Tracker.services.DietService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/diets")
@Validated
public class DietController {

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    /**
     * POST - /api/diets, Rota de criação de uma dieta.
     * 
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<DietDto> saveDiet(@RequestBody @Valid DietRequestDto requestDto) {
        DietDto created = dietService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/diets, Rota que busca por todas as dietas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<DietDto>> getAllDiets() {
        return ResponseEntity.status(HttpStatus.OK).body(dietService.findAll());
    }

    /**
     * GET - /api/diets/id, Rota que busca uma dieta a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<DietDto> getOneDiet(@PathVariable UUID id) {
        return dietService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/diets/id, Rota que atualiza uma dieta
     * 
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<DietDto> updateDiet(
            @PathVariable UUID id,
            @RequestBody @Valid DietRequestDto requestDto) {

        return dietService.update(id, requestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE - /api/diets/id, Rota que deleta uma dieta
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiet(@PathVariable UUID id) {
        boolean deleted = dietService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}