package com.cts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.constant.LoginType;
import com.cts.dto.LoginRequestDTO;
import com.cts.dto.LoginResponseDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request,
            @RequestHeader("loginType") LoginType loginType) {

        log.info("Login request for {} with role {}", request.getUsername(), loginType);

        String username = request.getUsername();
        String password = request.getPassword();

        // ✅ Dummy hardcoded login logic
        if (loginType == LoginType.STUDENT && username.equals("student123") && password.equals("pass123")) {
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .userId(101L)
                    .role("STUDENT")
                    .token("student-token-123")
                    .build();
            return ResponseEntity.ok(response);
        }

        if (loginType == LoginType.DRIVER && username.equals("driver123") && password.equals("pass123")) {
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .userId(201L)
                    .role("DRIVER")
                    .token("driver-token-123")
                    .build();
            return ResponseEntity.ok(response);
        }

        if (loginType == LoginType.ADMIN && username.equals("admin123") && password.equals("pass123")) {
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .userId(301L)
                    .role("ADMIN")
                    .token("admin-token-123")
                    .build();
            return ResponseEntity.ok(response);
        }

        // ❌ Invalid login
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
