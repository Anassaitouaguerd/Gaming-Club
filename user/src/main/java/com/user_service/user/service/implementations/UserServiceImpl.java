package com.user_service.user.service.implementations;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.Role;
import com.user_service.user.entity.User;
import com.user_service.user.mapper.UserMapper;
import com.user_service.user.repository.RoleRepository;
import com.user_service.user.repository.UserRepository;
import com.user_service.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }

        User user = userMapper.toEntity(userDTO);
        String defaultPassword = generateDefaultPassword(user.getFirstName());
        if(user.getUsername() == null){
            String username = generateDefaultUsername(user.getFirstName() , user.getLastName());
            user.setUsername(username);
        }
        user.setPassword(passwordEncoder.encode(defaultPassword));

        Role memberRole = roleRepository.findByName(userDTO.role().name())
                .orElseThrow(() -> new RuntimeException("Default MEMBER role not found"));
        user.setRole(memberRole);

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long userID, UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        String defaultPassword = generateDefaultPassword(user.getFirstName());
        Role memberRole = roleRepository.findByName(userDTO.role().name())
                .orElseThrow(() -> new RuntimeException("Default MEMBER role not found"));
        user.setId(userID);
        user.setRole(memberRole);
        user.setPassword(passwordEncoder.encode(defaultPassword));

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("this user is not found "));
        userRepository.delete(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    private String generateDefaultPassword(String firstName) {
        return firstName + "2025";
    }

    private String generateDefaultUsername(String firstName , String lastName){
        return firstName + "_" + lastName;
    }
}