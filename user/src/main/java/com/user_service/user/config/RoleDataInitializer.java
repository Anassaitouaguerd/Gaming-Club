package com.user_service.user.config;

import com.user_service.user.entity.Role;
import com.user_service.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RoleDataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // Define the default roles
            List<String> defaultRoles = Arrays.asList("ADMIN", "STAFF", "MEMBER", "GUEST");

            // Check if roles table is empty
            if (roleRepository.count() == 0) {
                // Create and save each role
                defaultRoles.forEach(roleName -> {
                    Role role = Role.builder()
                            .name(roleName)
                            .build();
                    roleRepository.save(role);
                });

                System.out.println("Default roles have been initialized");
            } else {
                System.out.println("Roles are already initialized");
            }
        };
    }
}