package com.ams.security.controller;

import com.ams.security.dto.LoginDto;
import com.ams.security.service.AuthService;
import com.ams.security.service.RefreshTokenService;
import com.ams.entity.User;
import com.ams.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody LoginDto dto) {
		return authService.login(dto);
	}

	@PostMapping("/refresh")
	public Map<String, String> refreshToken(@RequestBody Map<String, String> request) {
		String refreshToken = request.get("refreshToken");
		User user = refreshTokenService.getUserFromToken(refreshToken, userService);
		if (!refreshTokenService.isValid(refreshToken, user)) {
			log.warn("Invalid refresh token attempt for user: {}", user.getUsername());
			throw new RuntimeException("Invalid refresh token");
		}
		String jwt = refreshTokenService.createOrGetRefreshToken(user); // reissue access token
		return Map.of("jwt", jwt);
	}

	@PostMapping("/logout")
	public String logout(@RequestBody Map<String, String> request) {
		String refreshToken = request.get("refreshToken");
		User user = refreshTokenService.getUserFromToken(refreshToken, userService);
		refreshTokenService.deleteRefreshToken(user);
		log.info("User {} logged out", user.getUsername());
		return "Logged out successfully";
	}
}
