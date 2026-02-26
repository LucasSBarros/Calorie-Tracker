package com.calorietracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;

public interface StatusService {

    /**
     * Método de criação de um status
     * 
     * @param request
     * @return
     */
    StatusDto create(StatusRequestDto request);

    /**
     * Método de listagem dos status
     * 
     * @return
     */
    List<StatusDto> findAll();

    /**
     * Método de busca de status por Id
     * 
     * @param id
     * @return
     */
    Optional<StatusDto> findById(UUID id);

    /**
     * Método de atualização de um status
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<StatusDto> update(UUID id, StatusRequestDto request);

    /**
     * Método de deletar um status
     * 
     * @param id
     * @return
     */
    boolean delete(UUID id);
}