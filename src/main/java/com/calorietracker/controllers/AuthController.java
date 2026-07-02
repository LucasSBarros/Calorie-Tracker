package com.calorietracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.calorietracker.dtos.response.AuthResponse;
import com.calorietracker.dtos.request.LoginRequest;
import com.calorietracker.dtos.request.RegisterRequest;
import com.calorietracker.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST - /api/auth/register, Rota de cadastro de usuário.
     * 
     * @param request
     * @return token de autenticação
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * POST - /api/auth/login, Rota de autenticação do usuário.
     * 
     * @param request
     * @return token de autenticação
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}