package com.user_service.user.controller.crud;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;
import com.user_service.user.repository.UserRepository;
import com.user_service.user.service.interfaces.AuthService;
import com.user_service.user.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.addUser(userDTO));
    }

    @PutMapping("/update/{userID}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userID, @Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(userID, userDTO));
    }

    @DeleteMapping("/delete/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userID) {
        userService.deleteUser(userID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.of(Optional.ofNullable(userService.getAllUsers()));
    }

    @GetMapping("/get/byEmail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(authService.findUserByEmail(email));
    }

    @GetMapping("/get/byUserName/{username}")
    public ResponseEntity<UserDTO> getUserByUserName(@PathVariable String username){
        return ResponseEntity.ok(authService.findUserByUsername(username));
    }

    // New endpoints for OpenFeign integration
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}