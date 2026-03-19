package com.calorietracker.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;
import com.calorietracker.dtos.UserProgressDto;
import com.calorietracker.services.StatusService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * POST - /api/status
     * Rota de criação de um status.
     */
    @PostMapping
    public ResponseEntity<StatusDto> saveStatus(@RequestBody @Valid StatusRequestDto requestDto) {
        StatusDto created = statusService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/status
     * Rota que busca por todos os status.
     */
    @GetMapping
    public ResponseEntity<List<StatusDto>> getAllStatus() {
        return ResponseEntity.ok(statusService.findAll());
    }

    /**
     * GET - /api/status/{id}
     * Rota que busca um status a partir do seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusDto> getOneStatus(@PathVariable UUID id) {
        return statusService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/status/{id}
     * Rota que atualiza um status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StatusDto> updateStatus(
            @PathVariable UUID id,
            @RequestBody @Valid StatusRequestDto requestDto) {

        return statusService.update(id, requestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE - /api/status/{id}
     * Rota que deleta um status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable UUID id) {
        statusService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET - /api/status/progress/{userId}
     * Rota que exibe o progresso percentual do usuário em relação à meta.
     */
    @GetMapping("/progress/{userId}")
    public ResponseEntity<UserProgressDto> getUserProgress(@PathVariable UUID userId) {
        return statusService.getUserProgress(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET - /api/status/history/{userId}
     * Rota que exibe o histórico de status de um usuário.
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<StatusDto>> getUserHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(statusService.findHistoryByUser(userId));
    }

    /**
     * GET - /api/status/history/{userId}/period?start=...&end=...
     * Rota que busca o histórico de status por usuário e período.
     */
    @GetMapping("/history/{userId}/period")
    public ResponseEntity<List<StatusDto>> getUserHistoryByPeriod(
            @PathVariable UUID userId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(statusService.findByUserAndPeriod(userId, start, end));
    }
}