package com.user_service.user.dto.user;

import com.user_service.user.entity.enums.Role;

public record UserDTO(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        String phoneNumber){}
