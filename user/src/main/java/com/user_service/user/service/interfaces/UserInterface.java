package com.user_service.user.service.interfaces;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;

import java.util.List;

public interface UserInterface {
    public User addUser(UserDTO userDTO);
    public User updateUser(Long userID , UserDTO userDTO);
    public void deleteUser(Long userID);
    public List<User> getAllUsers();

}
