package com.ams.security.service;

import com.ams.entity.User;
import com.ams.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User not found: {}", username);
            return new UsernameNotFoundException("User not found: " + username);
        });

        log.info("Loaded user successfully: {}", user.getUsername());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash()) // encoded password
                .roles(user.getRole().name())
                .disabled(user.getStatus() != User.AccountStatus.ACTIVE)
                .build();
    }
}
