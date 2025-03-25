package com.user_service.user.dto.user;

public record UserCreatedDTO(
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String password
) {
}
