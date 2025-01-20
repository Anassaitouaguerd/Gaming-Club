package com.user_service.user.controller.crud;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.User;
import com.user_service.user.service.implementations.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.addUser(userDTO));
    }

    @PutMapping("/update/{userID}")
    public ResponseEntity<User> updateUser(@PathVariable Long userID, @Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(userID , userDTO));
    }

    @DeleteMapping("/delete/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userID) {
        userService.deleteUser(userID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.of(Optional.ofNullable(userService.getAllUsers()));
    }
}
