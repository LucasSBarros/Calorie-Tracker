package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.GoalDto;
import com.calorietracker.dtos.GoalRequestDto;
import com.calorietracker.services.GoalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/goals")
@Validated
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /**
     * POST - /api/goals, Rota de criação de uma goala.
     * 
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<GoalDto> saveGoal(@RequestBody @Valid GoalRequestDto requestDto) {
        GoalDto created = goalService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/goals, Rota que busca por todas as goalas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<GoalDto>> getAllGoals() {
        return ResponseEntity.status(HttpStatus.OK).body(goalService.findAll());
    }

    /**
     * GET - /api/goals/id, Rota que busca uma goala a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoalDto> getOneGoal(@PathVariable UUID id) {
        return goalService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/goals/id, Rota que atualiza uma goala
     * 
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoalDto> updateGoal(
            @PathVariable UUID id,
            @RequestBody @Valid GoalRequestDto requestDto) {

        return goalService.update(id, requestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE - /api/goals/id, Rota que deleta uma goala
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        boolean deleted = goalService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}