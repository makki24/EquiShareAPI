package org.example.fakeportfolios.service;

import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User getLoggedInUser() {
        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Return the principal (UserDetails) of the logged-in user
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername()).get();
        } else {
            return null;  // In case the authentication object is null or not UserDetails
        }
    }
}
