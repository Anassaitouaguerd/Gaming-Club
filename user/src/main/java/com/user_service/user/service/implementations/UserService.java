package com.user_service.user.service.implementations;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.Role;
import com.user_service.user.entity.User;
import com.user_service.user.mapper.UserMapper;
import com.user_service.user.repository.RoleRepository;
import com.user_service.user.repository.UserRepository;
import com.user_service.user.service.interfaces.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User addUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }

        User user = UserMapper.INSTANCE.toEntity(userDTO);
        String defaultPassword = generateDefaultPassword(user.getFirstName());
        user.setPassword(passwordEncoder.encode(defaultPassword));

        Role memberRole = roleRepository.findByName(userDTO.role().name())
                .orElseThrow(() -> new RuntimeException("Default MEMBER role not found"));
        user.setRole(memberRole);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userID , UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        String defaultPassword = generateDefaultPassword(user.getFirstName());
        Role memberRole = roleRepository.findByName(userDTO.role().name())
                .orElseThrow(() -> new RuntimeException("Default MEMBER role not found"));
        user.setId(userID);
        user.setRole(memberRole);
        user.setPassword(passwordEncoder.encode(defaultPassword));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("this user is not found "));
        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private String generateDefaultPassword(String firstName) {
        return firstName + "2025";
    }
}