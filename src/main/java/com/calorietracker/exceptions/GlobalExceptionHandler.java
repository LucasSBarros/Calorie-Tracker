package com.calorietracker.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Trata exceções de negócio genéricas.
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiError> handleBusinessException(
                        BusinessException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Business Error",
                                ex.getMessage(),
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.badRequest().body(error);
        }

        /**
         * Trata recurso não encontrado.
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        /**
         * Trata erros de conflito.
         */
        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ApiError> handleConflict(
                        ConflictException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                ex.getMessage(),
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        /**
         * Trata exceções de autenticação não autorizada.
         */
        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ApiError> handleUnauthorizedException(
                        UnauthorizedException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized",
                                ex.getMessage(),
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        /**
         * Trata falhas de autenticação do Spring Security.
         */
        @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
        public ResponseEntity<ApiError> handleAuthenticationExceptions(
                        RuntimeException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized",
                                "Invalid credentials",
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        /**
         * Trata falhas de autorização de acesso.
         */
        @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
        public ResponseEntity<ApiError> handleAccessDeniedException(
                        org.springframework.security.access.AccessDeniedException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.FORBIDDEN.value(),
                                "Forbidden",
                                "Access denied",
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        /**
         * Trata erros de validação dos DTOs anotados com @Valid.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                ApiError error = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                message,
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.badRequest().body(error);
        }

        /**
         * Trata erros inesperados.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGenericException(
                        Exception ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                ex.getMessage(),
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
}