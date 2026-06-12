package com.healthtracker.backend.service;

import com.healthtracker.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String jwtSecret;
    private final long accessTokenExpirationSeconds;

    public JwtService(
            @Value("${security.jwt.secret:dev-only-healthtracker-jwt-secret-change-me-32chars}") String jwtSecret,
            @Value("${security.jwt.access-token-expiration-seconds:1800}") long accessTokenExpirationSeconds
    ) {
        this.jwtSecret = jwtSecret;
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(accessTokenExpirationSeconds);

        return Jwts.builder()
                .subject(user.getEmail())
                // Guardamos el userId para no confiar en IDs enviados por el cliente.
                .claim("userId", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey())
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String expectedEmail) {
        String tokenEmail = extractEmail(token);
        return tokenEmail.equals(expectedEmail) && parseClaims(token).getExpiration().after(new Date());
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
