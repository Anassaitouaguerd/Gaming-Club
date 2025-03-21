package com.user_service.user.service.interfaces;

import com.user_service.user.dto.user.UserCreatedDTO;
import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;

public interface AuthService {
    User registerUser(UserCreatedDTO userCreatedDTO);
    UserDTO findUserByUsername(String username);
    UserDTO findUserByEmail(String email);
    UserDTO login(String username, String password);
}
