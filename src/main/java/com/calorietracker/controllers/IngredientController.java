package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.response.IngredientResponse;
import com.calorietracker.dtos.request.IngredientRequest;
import com.calorietracker.services.IngredientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * POST - /api/ingredients, Rota de criação de uma ingredienta.
     * 
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<IngredientResponse> saveIngredient(@RequestBody @Valid IngredientRequest request) {
        var created = ingredientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/ingredients, Rota que busca por todas as ingredientas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.findAll());
    }

    /**
     * GET - /api/ingredients/id, Rota que busca uma ingredienta a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getOneIngredient(@PathVariable UUID id) {
        return ingredientService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/ingredients/id, Rota que atualiza uma ingredienta
     * 
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(
            @PathVariable UUID id,
            @RequestBody @Valid IngredientRequest request) {

        return ingredientService.update(id, request)
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
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
