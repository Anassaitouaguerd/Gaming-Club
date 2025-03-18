package com.user_service.user.dto.user;

import com.user_service.user.entity.enums.Role;

public record UserCreatedDTO(
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String password
) {
}
