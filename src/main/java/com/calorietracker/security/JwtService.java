package com.calorietracker.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.calorietracker.config.JwtProperties;
import com.calorietracker.models.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    /**
     * Método responsável por gerar um token JWT para o usuário autenticado.
     * 
     * @param user
     * @return
     */
    public String generateToken(UserModel user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.expiration()))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Método responsável por extrair o email do usuário a partir do token JWT.
     * 
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Método responsável por validar o token JWT com base no usuário e na
     * expiração.
     * 
     * @param token
     * @param user
     * @return true se válido, false caso contrário
     */
    public boolean isTokenValid(String token, UserModel user) {
        String username = extractUsername(token);
        return username.equals(user.getEmail()) && !isTokenExpired(token);
    }

    /**
     * Método responsável por verificar se o token JWT está expirado.
     * 
     * @param token
     * @return true se expirado, false caso contrário
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Método responsável por extrair todas as informações (claims) do token JWT.
     * 
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Método responsável por gerar a chave de assinatura do token JWT.
     * 
     * @return
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}