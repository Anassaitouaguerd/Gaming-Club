package com.user_service.user.service.interfaces;

import com.user_service.user.dto.user.UserDTO;

import java.util.List;

public interface UserService {
    public UserDTO addUser(UserDTO userDTO);
    public UserDTO updateUser(Long userID, UserDTO userDTO);
    public void deleteUser(Long userID);
    public List<UserDTO> getAllUsers();
}