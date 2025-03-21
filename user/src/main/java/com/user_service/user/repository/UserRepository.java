package com.user_service.user.repository;

import com.user_service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
