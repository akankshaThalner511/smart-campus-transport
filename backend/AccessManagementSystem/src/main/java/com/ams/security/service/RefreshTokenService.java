package com.ams.security.service;

import com.ams.entity.User;
import com.ams.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-expiration-ms:86400000}") // fallback 24h
    private long refreshTtlMs;

    @Value("${jwt.redis-prefix:jwt:refresh:}")
    private String redisPrefix;

    private Duration ttl() {
        return Duration.ofMillis(refreshTtlMs);
    }

    public String createOrGetRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(redisKey(user.getUsername()), token, ttl());
        log.info("Created refresh token for user: {}", user.getUsername());
        return token;
    }

    public boolean isValid(String token, User user) {
        String saved = redisTemplate.opsForValue().get(redisKey(user.getUsername()));
        boolean valid = saved != null && saved.equals(token);
        if (!valid) log.warn("Invalid/expired refresh token for user: {}", user.getUsername());
        return valid;
    }

    public User getUserFromToken(String token, UserService userService) {
        var ops = redisTemplate.opsForValue();
        var keys = redisTemplate.keys(redisPrefix + "*");
        if (keys != null) {
            for (String key : keys) {
                String value = ops.get(key);
                if (token.equals(value)) {
                    // strip prefix to get username
                    String username = key.startsWith(redisPrefix) ? key.substring(redisPrefix.length()) : key;
                    return userService.getUserByUsername(username);
                }
            }
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public void deleteRefreshToken(User user) {
        redisTemplate.delete(redisKey(user.getUsername()));
        log.info("Deleted refresh token for user: {}", user.getUsername());
    }

    private String redisKey(String username) {
        return redisPrefix + username;
    }
}
