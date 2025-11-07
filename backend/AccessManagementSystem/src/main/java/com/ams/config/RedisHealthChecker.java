package com.ams.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Simple Redis connection health check.
 * Logs connection status at application startup.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisHealthChecker {

    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void checkRedisConnection() {
        try {
            // Simple ping test
            String pong = redisTemplate.getConnectionFactory()
                                       .getConnection()
                                       .ping();

            if ("PONG".equalsIgnoreCase(pong)) {
                log.info("✅ Redis connection established successfully.");
            } else {
                log.warn("⚠️ Redis connection test returned unexpected response: {}", pong);
            }
        } catch (Exception e) {
            log.error("❌ Unable to connect to Redis: {}", e.getMessage(), e);
        }
    }
}
