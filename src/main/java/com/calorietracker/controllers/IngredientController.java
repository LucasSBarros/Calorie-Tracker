package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.IngredientDto;
import com.calorietracker.dtos.IngredientRequestDto;
import com.calorietracker.services.IngredientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ingredients")
@Validated
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * POST - /api/ingredients, Rota de criação de uma ingredienta.
     * 
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<IngredientDto> saveIngredient(@RequestBody @Valid IngredientRequestDto requestDto) {
        IngredientDto created = ingredientService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/ingredients, Rota que busca por todas as ingredientas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.findAll());
    }

    /**
     * GET - /api/ingredients/id, Rota que busca uma ingredienta a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getOneIngredient(@PathVariable UUID id) {
        return ingredientService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/ingredients/id, Rota que atualiza uma ingredienta
     * 
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(
            @PathVariable UUID id,
            @RequestBody @Valid IngredientRequestDto requestDto) {

        return ingredientService.update(id, requestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE - /api/ingredients/id, Rota que deleta uma ingredienta
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable UUID id) {
        boolean deleted = ingredientService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}