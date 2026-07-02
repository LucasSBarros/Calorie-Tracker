package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.response.DietResponse;
import com.calorietracker.dtos.request.DietRequest;

public interface DietService {

    /**
     * Método de criação de uma dieta
     * 
     * @param request
     * @return
     */
    DietResponse create(DietRequest request);

    /**
     * Método de listagem das dietas
     * 
     * @return
     */
    List<DietResponse> findAll();

    /**
     * Método de busca de dieta por Id
     * 
     * @param id
     * @return
     */
    Optional<DietResponse> findById(UUID id);

    /**
     * Método de atualização de uma dieta
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<DietResponse> update(UUID id, DietRequest request);

    /**
     * Método de deletar uma dieta
     * 
     * @param id
     * @return
     */
    void delete(UUID id);
}