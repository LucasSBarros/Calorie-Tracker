package com.calorietracker.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.calorietracker.dtos.response.StatusResponse;
import com.calorietracker.dtos.request.StatusRequest;
import com.calorietracker.dtos.response.ProgressResponse;

public interface StatusService {

    /**
     * Método de criação de um status
     * 
     * @param request
     * @return
     */
    StatusResponse create(StatusRequest request);

    /**
     * Método de listagem dos status
     * 
     * @return
     */
    List<StatusResponse> findAll();

    /**
     * Método de busca de status por Id
     * 
     * @param id
     * @return
     */
    Optional<StatusResponse> findById(UUID id);

    /**
     * Método de atualização de um status
     * 
     * @param id
     * @param request
     * @return
     */
    Optional<StatusResponse> update(UUID id, StatusRequest request);

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
    Optional<ProgressResponse> getUserProgress(UUID userId);

    /**
     * Método para mostrat o histórico do usuário
     * 
     * @param userId
     * @return
     */
    List<StatusResponse> findHistoryByUser(UUID userId);

    /**
     * Método para pesquisar o histório pela data e por usuário
     * 
     * @param userId
     * @param start
     * @param end
     * @return
     */
    List<StatusResponse> findByUserAndPeriod(UUID userId, LocalDateTime start, LocalDateTime end);

}