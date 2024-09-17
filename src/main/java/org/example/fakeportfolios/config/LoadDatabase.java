package org.example.fakeportfolios.config;

import org.example.fakeportfolios.constants.constants.Roles;
import org.example.fakeportfolios.model.Role;
import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.RoleRepository;
import org.example.fakeportfolios.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Configuration
public class LoadDatabase {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "makki24";
            String initialPassword = "password"; // Set the initial password
            Set<String> rolesList = new HashSet<>(List.of(Roles.ADMIN)); // Roles.ADMIN = "ADMIN"

            // Check if the user exists
            Optional<User> adminOptional = userRepository.findByUsername(username);
            User newAdmin;

            if (adminOptional.isPresent()) {
                newAdmin = adminOptional.get();
            } else {
                // Create a new user if not exists
                newAdmin = new User();
                newAdmin.setUsername(username);
                newAdmin.setPassword(passwordEncoder.encode(initialPassword)); // Set and encode the password
                newAdmin.setRoles(new HashSet<>()); // Initialize the roles set
                newAdmin.setEnabled(true);
                userRepository.save(newAdmin); // Save the newly created user
            }

            // Check and assign roles
            for (String roleName : rolesList) {
                Optional<Role> roleOptional = roleRepository.findByName(roleName);
                Role role;

                if (roleOptional.isPresent()) {
                    role = roleOptional.get();
                } else {
                    // Create a new role if it doesn't exist
                    role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role); // Save the new role
                }

                // Add role to user if not already present
                newAdmin.getRoles().add(role);
            }

            // Finally, save the updated user with assigned roles
            userRepository.save(newAdmin);
        };
    }
}
