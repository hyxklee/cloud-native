package com.example.demo.domain.user.dto.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
