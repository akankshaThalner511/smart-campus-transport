package com.ams.security.dto;

import com.ams.entity.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
    private User.Role role;

    @JsonCreator
    public LoginDto(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("role") String role
    ) {
        this.username = username;
        this.password = password;

        if (role != null) {
            try {
                this.role = User.Role.valueOf(role.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                this.role = null;
            }
        } else {
            this.role = null;
        }
    }

    @Override
    public String toString() {
        return String.format("LoginDto(username=%s, role=%s)", username, role);
    }
}
