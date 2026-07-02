package com.calorietracker.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calorietracker.dtos.response.AuthResponse;
import com.calorietracker.dtos.request.LoginRequest;
import com.calorietracker.dtos.request.RegisterRequest;
import com.calorietracker.exceptions.ConflictException;
import com.calorietracker.exceptions.UnauthorizedException;
import com.calorietracker.models.UserModel;
import com.calorietracker.repositories.UserRepository;
import com.calorietracker.security.JwtService;
import com.calorietracker.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already in use: " + request.email());
        }

        var user = new UserModel();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setWeight(request.weight());
        user.setHeight(request.height());
        user.setBirthDate(request.birthDate());
        user.setGender(request.gender());

        var saved = userRepository.save(user);

        var token = jwtService.generateToken(saved);

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()));
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid credentials");
        }

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        var token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
