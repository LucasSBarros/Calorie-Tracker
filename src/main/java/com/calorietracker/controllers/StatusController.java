package com.calorietracker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;
import com.calorietracker.services.StatusService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/statuss")
@Validated
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * POST - /api/statuss, Rota de criação de uma statusa.
     * 
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<StatusDto> saveStatus(@RequestBody @Valid StatusRequestDto requestDto) {
        StatusDto created = statusService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET - /api/statuss, Rota que busca por todas as statusas.
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<StatusDto>> getAllStatuss() {
        return ResponseEntity.status(HttpStatus.OK).body(statusService.findAll());
    }

    /**
     * GET - /api/statuss/id, Rota que busca uma statusa a partir do seu ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<StatusDto> getOneStatus(@PathVariable UUID id) {
        return statusService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT - /api/statuss/id, Rota que atualiza uma statusa
     * 
     * @param id
     * @param requestDto
     * @return
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
     * DELETE - /api/statuss/id, Rota que deleta uma statusa
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable UUID id) {
        boolean deleted = statusService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}