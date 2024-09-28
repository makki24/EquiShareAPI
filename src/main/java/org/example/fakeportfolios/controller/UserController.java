package org.example.fakeportfolios.controller;

import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.UserRepository;
import org.example.fakeportfolios.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/{userId}/wallet")
    public ResponseEntity<?> addToWallet(@PathVariable Long userId, @RequestParam double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setCurrentAmount(user.getCurrentAmount() + amount);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/getLoggedInUser")
    public User getLoggedInUser() {
        return userService.getLoggedInUser();
    }

}
