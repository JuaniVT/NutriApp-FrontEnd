package com.NutriApp.NutriApp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    // Clave secreta para firmar el token. ¡Nunca la subas a GitHub! Mejor usar variables de entorno.
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Genera un token para un usuario autenticado
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // El "dueño" del token (ej: nombre de usuario)
                .claim("roles", userDetails.getAuthorities()) // Información adicional: los roles del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Cuándo fue generado
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira en 1 hora
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma el token con HS256 y la clave secreta
                .compact(); // Lo convierte a una cadena tipo JWT (header.payload.firma)
    }

    //  Verifica si un token es válido para un usuario dado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // extrae el usuario desde el token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Extrae el nombre de usuario desde el token (campo "sub")
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Verifica si el token está vencido (exp < fecha actual)
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extrae todos los datos (claims) del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey()) // Le dice con qué clave validar la firma
                .build()
                .parseClaimsJws(token) // Parsea el JWT completo y lo valida
                .getBody(); // Devuelve el contenido (payload)
    }

    //Convierte la clave secreta en un objeto Key para usar con la librería jjwt
    private Key getKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes); // Usa HMAC con SHA-256
    }
}
