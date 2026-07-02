package com.calorietracker.services;

import com.calorietracker.dtos.response.AuthResponse;
import com.calorietracker.dtos.request.LoginRequest;
import com.calorietracker.dtos.request.RegisterRequest;

public interface AuthService {

    /**
     * Método responsável por registrar um novo usuário e gerar o token de
     * autenticação.
     * 
     * @param request
     * @return token de autenticação
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Método responsável por autenticar um usuário e gerar o token JWT.
     * 
     * @param request
     * @return token de autentação
     */
    AuthResponse login(LoginRequest request);
}