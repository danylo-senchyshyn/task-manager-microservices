package com.example.userservice.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    /// Берёт значение переменной jwt.secret из application.properties или .yml.
    /// Это секретный ключ, которым ты подписываешь токен
    @Value("${jwt.secret}")
    private String secret;

    /// 24 hours in milliseconds
    private final long expirationTime = 1000 * 60 * 60 * 24;

    /// Получает username из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    ///  Универсальный способ вытащить любой claim из токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /// Создаёт токен
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /// Проверяет:
    /// •	Что username из токена совпадает с текущим пользователем;
    /// •	Что токен не истёк.
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /// Проверяет, истёк ли токен
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /// Получает дату истечения токена
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /// Парсит токен и возвращает его содержимое (claims = полезная информация)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
