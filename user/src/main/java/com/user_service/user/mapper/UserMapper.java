package com.user_service.user.mapper;

import com.user_service.user.dto.user.UserCreatedDTO;
import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.Role;
import com.user_service.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        String roleName = null;
        if (user.getRole() != null) {
            roleName = user.getRole().getName();
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roleName != null ? com.user_service.user.entity.enums.Role.valueOf(roleName) : null,
                user.getPhoneNumber()
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());

        return user;
    }

    public User toUserEntity(UserCreatedDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());

        return user;
    }


    public Role createRole(String roleName) {
        if (roleName == null) {
            return null;
        }
        return Role.builder().name(roleName).build();
    }
}