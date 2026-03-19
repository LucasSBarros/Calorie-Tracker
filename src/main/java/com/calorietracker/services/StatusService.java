package com.calorietracker.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.StatusDto;
import com.calorietracker.dtos.StatusRequestDto;
import com.calorietracker.dtos.UserProgressDto;

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
    void delete(UUID id);

    /**
     * Método para exibir o progresso do usuário
     * 
     * @param userId
     * @return
     */
    Optional<UserProgressDto> getUserProgress(UUID userId);

    /**
     * Método para mostrat o histórico do usuário
     * 
     * @param userId
     * @return
     */
    List<StatusDto> findHistoryByUser(UUID userId);

    /**
     * Método para pesquisar o histório pela data e por usuário
     * 
     * @param userId
     * @param start
     * @param end
     * @return
     */
    List<StatusDto> findByUserAndPeriod(UUID userId, LocalDateTime start, LocalDateTime end);

}