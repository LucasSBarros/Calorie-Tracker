package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.response.GoalResponse;
import com.calorietracker.dtos.request.GoalRequest;
import com.calorietracker.services.GoalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /**
     * POST - /api/goals, Rota de criação de um objetivo.
     * 
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<GoalResponse> saveGoal(@RequestBody @Valid GoalRequest request) {
        var created = goalService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/goals, Rota que busca por todos os objetivos.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAllGoals() {
        return ResponseEntity.status(HttpStatus.OK).body(goalService.findAll());
    }

    /**
     * GET - /api/goals/id, Rota que busca um objetivo a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getOneGoal(@PathVariable UUID id) {
        return goalService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/goals/id, Rota que atualiza um objetivo
     * 
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(
            @PathVariable UUID id,
            @RequestBody @Valid GoalRequest request) {

        return goalService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE - /api/goals/id, Rota que deleta um objetivo
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
