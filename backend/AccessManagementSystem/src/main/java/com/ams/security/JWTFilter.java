package com.ams.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ams.security.service.CustomUserDetailsService;
import com.ams.security.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWTFilter intercepts incoming HTTP requests to validate the JWT token.
 * <p>
 * If a valid JWT is found in the Authorization header, it sets the
 * authentication in the Spring Security context so that secured endpoints can
 * be accessed.
 * </p>
 * <p>
 * Key Updates:
 * <ul>
 * <li>Uses Lombok's {@link Slf4j} for logging instead of manually managing a
 * logger.</li>
 * <li>Java 17 style using <code>var</code> for local variable type
 * inference.</li>
 * <li>Includes Javadoc for clear documentation.</li>
 * <li>Keeps JWT validation and SecurityContext setup intact.</li>
 * </ul>
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		// Extract token from Authorization header
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			try {
				username = jwtUtil.getUsernameFromToken(token);
			} catch (Exception e) {
				log.error("JWT error: {}", e.getMessage());
			}
		}

		// Validate token and set authentication
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			var userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(token)) {
				var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		// Continue filter chain
		filterChain.doFilter(request, response);
	}
}
