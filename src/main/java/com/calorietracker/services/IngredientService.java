package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.IngredientDto;
import com.calorietracker.dtos.IngredientRequestDto;

public interface IngredientService {

    /**
     * Método de criação de um ingrediente
     * 
     * @param request
     * @return
     */
    IngredientDto create(IngredientRequestDto request);

    /**
     * Método de listagem de ingredientes
     * 
     * @return
     */
    List<IngredientDto> findAll();

    /**
     * Método de busca de ingredientes por Id
     * 
     * @param id
     * @return
     */
    Optional<IngredientDto> findById(UUID id);

    /**
     * Método de atualização de um ingredientes
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<IngredientDto> update(UUID id, IngredientRequestDto request);

    /**
     * Método de deletar um ingredientes
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}