package com.user_service.user.controller.auth;

import com.user_service.user.dto.auth.LoginRequest;
import com.user_service.user.dto.user.UserCreatedDTO;
import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;
import com.user_service.user.mapper.UserMapper;
import com.user_service.user.security.JwtTokenProvider;
import com.user_service.user.service.implementations.AuthServiceImpl;
import com.user_service.user.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserCreatedDTO userCreatedDTO) {
        User registeredUser = authService.registerUser(userCreatedDTO);
        return new ResponseEntity<>(userMapper.toDTO(registeredUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and generate JWT token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDTO user = authService.findUserByEmail(loginRequest.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(user.username());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("role", user.role().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout a user")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/user")
    @Operation(summary = "Get user details by username")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO user = authService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getCurrentUser/{token}")
    @Operation(summary = "Get Current user by decoding JWT")
    public ResponseEntity<UserDTO> getCurrentUser(@PathVariable String token){
        String username = jwtTokenProvider.getUsernameFromToken(token);
        UserDTO user = authService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}