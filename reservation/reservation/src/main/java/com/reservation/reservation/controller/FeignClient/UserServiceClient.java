package com.reservation.reservation.controller.FeignClient;

import com.reservation.reservation.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user/username/{username}")
    Optional<UserDto> findByUsername(@PathVariable("username") String username);

    @GetMapping("/user/{id}")
    Optional<UserDto> findById(@PathVariable("id") Long id);
}