package com.user_service.user.service.interfaces;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;

public interface AuthService {
    User registerUser(UserDTO userDTO);
    User findUserByUsername(String username);
    UserDTO login(String username, String password);
}
