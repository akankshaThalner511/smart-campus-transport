package com.ams.security.util;

import com.ams.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for generating, parsing, and validating JWT tokens for users.
 * Uses userId as the primary claim (sub) for immutable identity.
 */
@Component
@Slf4j
public class JWTUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key signingKey;

    /**
     * Returns a secure signing key.
     * Ensures the secret key meets minimum 256-bit size requirements for HS algorithms.
     */
    private Key getSigningKey() {
        if (signingKey == null) {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

            // Ensure key length is at least 64 bytes for HS512
            if (keyBytes.length < 64) {
                log.warn("JWT secret key is too short ({} bytes). Padding to 64 bytes for HS512 security compliance.", keyBytes.length);
                byte[] paddedKey = new byte[64];
                System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 64));
                keyBytes = paddedKey;
            }

            signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return signingKey;
    }

    /**
     * Generates a JWT token for the given user.
     * Uses userId as the 'sub' (primary identifier) and includes role & username.
     *
     * @param user The user object
     * @return JWT token string
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId())) // userId as sub
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtExpirationMs)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /** Extracts userId (sub) from token */
    public Integer getUserIdFromToken(String token) {
        return Integer.valueOf(parseClaims(token).getSubject());
    }

    /** Extracts username claim from token */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).get("username", String.class);
    }

    /** Extracts role claim from token */
    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /** Parses JWT and returns claims */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("JWT expired: {}", e.getMessage());
            throw new RuntimeException("JWT token expired", e);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /** Validates token signature and expiration */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
