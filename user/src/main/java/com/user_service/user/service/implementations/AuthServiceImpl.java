package com.user_service.user.service.implementations;

import com.user_service.user.dto.user.UserCreatedDTO;
import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;
import com.user_service.user.entity.Role;
import com.user_service.user.mapper.UserMapper;
import com.user_service.user.repository.RoleRepository;
import com.user_service.user.repository.UserRepository;
import com.user_service.user.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid user credentials"));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid user credentials"));
        return userMapper.toDTO(user);
    }

    @Override
    public User registerUser(UserCreatedDTO userCreatedDTO) {
        User user = userMapper.toUserEntity(userCreatedDTO);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(userCreatedDTO.password()));

        Role role = roleRepository.findByName("GUEST")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return userMapper.toDTO(user);
    }
}