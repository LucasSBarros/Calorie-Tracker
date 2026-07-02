package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.response.IngredientResponse;
import com.calorietracker.dtos.request.IngredientRequest;

public interface IngredientService {

    /**
     * Método de criação de um ingrediente
     * 
     * @param request
     * @return
     */
    IngredientResponse create(IngredientRequest request);

    /**
     * Método de listagem de ingredientes
     * 
     * @return
     */
    List<IngredientResponse> findAll();

    /**
     * Método de busca de ingredientes por Id
     * 
     * @param id
     * @return
     */
    Optional<IngredientResponse> findById(UUID id);

    /**
     * Método de atualização de um ingredientes
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<IngredientResponse> update(UUID id, IngredientRequest request);

    /**
     * Método de deletar um ingredientes
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}