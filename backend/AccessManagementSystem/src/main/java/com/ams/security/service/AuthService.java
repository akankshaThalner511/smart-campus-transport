package com.ams.security.service;

import com.ams.entity.User;
import com.ams.security.dto.LoginDto;
import com.ams.security.util.JWTUtil;
import com.ams.service.AdminService;
import com.ams.service.DriverService;
import com.ams.service.StudentService;
import com.ams.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final StudentService studentService;
    private final DriverService driverService;
    private final AdminService adminService;

    public Map<String, Object> login(LoginDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        User.Role requestedRole = dto.getRole();

        log.info("Incoming login request: username={}, requestedRole={}", username, requestedRole);

        // Authenticate credentials
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for {}: {}", username, ex.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }

        // Fetch user entity
        User user = userService.getUserByUsername(username);

        // Check if account is ACTIVE
        if (user.getStatus() != User.AccountStatus.ACTIVE) {
            log.warn("Inactive account login attempt: user={}", username);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is inactive");
        }

        // Role validation
        if (requestedRole != null && user.getRole() != requestedRole) {
            log.warn("❌ Role mismatch: user={} (actual={}) tried to login as={}", username, user.getRole(),
                     requestedRole);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role mismatch");
        }

        // Generate JWT token
        String jwt = jwtUtil.generateToken(user);

        // Generate refresh token
        String refreshToken;
        try {
            refreshToken = refreshTokenService.createOrGetRefreshToken(user);
        } catch (Exception e) {
            log.error("Redis unavailable, refresh token disabled for user {}: {}", username, e.getMessage());
            refreshToken = "TEMP_DISABLED";
        }

        // Role-specific payload
        Object payload = switch (user.getRole()) {
            case STUDENT -> studentService.getStudentByUser(user);
            case DRIVER -> driverService.getDriverByUser(user);
            case ADMIN -> adminService.getAdminByUser(user);
            default -> Map.of("message", "No payload available for role");
        };

        log.info("User {} authenticated successfully as {}", username, user.getRole());
        return Map.of(
                "jwt", jwt,
                "refreshToken", refreshToken,
                "role", user.getRole().name(),
                "payload", payload
        );
    }
}
