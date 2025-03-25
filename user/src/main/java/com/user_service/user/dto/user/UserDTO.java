package com.user_service.user.dto.user;

import com.user_service.user.entity.enums.RoleEnum;

public record UserDTO(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        RoleEnum role,
        String phoneNumber){}
