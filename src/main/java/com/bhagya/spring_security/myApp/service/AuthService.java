package com.bhagya.spring_security.myApp.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bhagya.spring_security.myApp.dto.AuthResponse;
import com.bhagya.spring_security.myApp.dto.LoginRequest;
import com.bhagya.spring_security.myApp.dto.SignupRequest;
import com.bhagya.spring_security.myApp.entity.AuthProviderType;
import com.bhagya.spring_security.myApp.entity.Role;
import com.bhagya.spring_security.myApp.entity.User;
import com.bhagya.spring_security.myApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(SignupRequest request) {
        try {
            userRepository.findByUsername(request.getUsername())
                    .ifPresent(user -> {
                        throw new RuntimeException("Username already exists");
                    });

            userRepository.findByEmail(request.getEmail())
                    .ifPresent(user -> {
                        throw new RuntimeException("Email already exists");
                    });

            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(Set.of(Role.USER))
                    .providerType(AuthProviderType.LOCAL)
                    .isEnabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

            return new AuthResponse("User registered successfully");

        } catch (RuntimeException e) {
            throw e;
        }

    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        return new AuthResponse("Login successful");
    }
}
