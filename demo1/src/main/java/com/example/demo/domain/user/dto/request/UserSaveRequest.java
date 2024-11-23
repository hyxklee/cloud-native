package com.example.demo.domain.user.dto.request;

public record UserSaveRequest(
        String name,
        String email,
        String password
) {
}
