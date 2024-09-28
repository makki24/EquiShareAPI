package org.example.fakeportfolios.controller;

import org.example.fakeportfolios.model.User;
import org.example.fakeportfolios.repository.UserRepository;
import org.example.fakeportfolios.service.JwtTokenProvider;
import org.example.fakeportfolios.dto.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")
public class LoginController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/hello")
    public  ResponseEntity<List<String>> hello() {
        return ResponseEntity.ok(new ArrayList<String>());
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
