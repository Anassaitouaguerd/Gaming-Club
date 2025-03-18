package com.user_service.user.dto.auth;

public record LoginResponse(
        String token,
        Long id,
        String username,
        String email,
        String role
) {}
