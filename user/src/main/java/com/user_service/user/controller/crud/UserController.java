package com.user_service.user.controller.crud;

import com.user_service.user.dto.user.UserDTO;
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
}