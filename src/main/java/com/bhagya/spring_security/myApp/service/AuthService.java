package com.bhagya.spring_security.myApp.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bhagya.spring_security.myApp.dto.LoginRequest;
import com.bhagya.spring_security.myApp.dto.LoginResponse;
import com.bhagya.spring_security.myApp.dto.SignupRequest;
import com.bhagya.spring_security.myApp.dto.SignupResponse;
import com.bhagya.spring_security.myApp.entity.AuthProviderType;
import com.bhagya.spring_security.myApp.entity.Role;
import com.bhagya.spring_security.myApp.entity.User;
import com.bhagya.spring_security.myApp.repository.UserRepository;
import com.bhagya.spring_security.myApp.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

/*  public AuthResponse basicSignup(SignupRequest request) {
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

            return new AuthResponse(user.getId(),"User registered successfully");

        } catch (RuntimeException e) {
            throw e;
        }

    }

    public AuthResponse basicLogin(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        return new AuthResponse(user.getId(),"Login successful");
    }
*/
    public SignupResponse jwtSignup(SignupRequest request) {
        try {
            // check if username or email already exists in the database, if yes throw an exception
            userRepository.findByUsername(request.getUsername())
                    .ifPresent(user -> {
                        throw new RuntimeException("Username already exists");
                    });

            userRepository.findByEmail(request.getEmail())
                    .ifPresent(user -> {
                        throw new RuntimeException("Email already exists");
                    });
            
            // create a new user and save to the database
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

            // return a response with basic user details and success message
            return new SignupResponse(user.getId(), request.getUsername(), "User registered successfully");

        } catch (RuntimeException e) {
            throw e;
        }

    }

    public LoginResponse jwtLogin(LoginRequest request) {

        // Authenticate the user using the provided credentials
        // Authentication authentication = authenticationManager.authenticate(
        //         new UsernamePasswordAuthenticationToken(
        //                 request.getUsername(),
        //                 request.getPassword()));

        // If authentication is successful, retrieve the user details
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        // create jwt token if authentication is successful and return in response
        String jwtToken = jwtUtils.generateToken(user);
        return new LoginResponse(jwtToken, user.getId(),"Login Successful");
    }

}
