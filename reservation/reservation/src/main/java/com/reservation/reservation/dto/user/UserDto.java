package com.reservation.reservation.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private RoleDto role;

    @Getter
    @Setter
    public static class RoleDto {
        private String name;
    }
}
