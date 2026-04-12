package com.bhagya.spring_security.myApp.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bhagya.spring_security.myApp.dto.LoginRequest;
import com.bhagya.spring_security.myApp.dto.LoginResponse;
import com.bhagya.spring_security.myApp.dto.SignupRequest;
import com.bhagya.spring_security.myApp.dto.SignupResponse;
import com.bhagya.spring_security.myApp.entity.AuthProviderType;
import com.bhagya.spring_security.myApp.entity.OAuthAccount;
import com.bhagya.spring_security.myApp.entity.Role;
import com.bhagya.spring_security.myApp.entity.User;
import com.bhagya.spring_security.myApp.repository.OAuthAccountsRepository;
import com.bhagya.spring_security.myApp.repository.UserRepository;
import com.bhagya.spring_security.myApp.security.JwtUtils2;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final AuthenticationManager authenticationManager;
    // private final JwtUtils jwtUtils;
    private final JwtUtils2 jwtUtils2;

    private final OAuthAccountsRepository oauthRepo;

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
                    // .providerType(AuthProviderType.LOCAL)
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
        // String jwtToken = jwtUtils.generateToken(user);
        String jwtToken = jwtUtils2.generateToken(user);
        return new LoginResponse(jwtToken, user.getId(),"Login Successful");
    }

    // for OAuth2

    public ResponseEntity<LoginResponse> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        // fetch providerType and providerId from the OAuth2User object
        // it the user has an account -> login and return jwt token
        // if the user does not have an account -> create an account(signup) and return jwt token(login)
        AuthProviderType providerType = jwtUtils2.getProviderTypeFromRegistrationId(registrationId);
        String providerId = jwtUtils2.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        // Check OAuth account - if user exists with current providerId and providerType, then login and return jwt token
        OAuthAccount account = oauthRepo.findByProviderTypeAndProviderId(providerType, providerId).orElse(null);
        if(account != null) {
            return ResponseEntity.ok(new LoginResponse(jwtUtils2.generateToken(account.getUser()), account.getUser().getId(), "Login successful"));
        }

        // Check if user exists with email
        String email = oAuth2User.getAttribute("email");

        if ((email == null || email.isBlank()) && registrationId.equalsIgnoreCase("github")) {
            email = providerId + "@oauth.local"; // can be improved by fetching email using GitHub API with the access token, but for simplicity we are using dummy email here
        }
        User user = userRepository.findByEmail(email).orElse(null);

        // Create user if not exists
        if(user == null) {
            user = User.builder()
                    .username(email)
                    .email(email)
                    .roles(Set.of(Role.USER))
                    .isEnabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }   

        // Link provider
        OAuthAccount newAccount = new OAuthAccount();
        newAccount.setProviderType(providerType);
        newAccount.setProviderId(providerId);
        newAccount.setUser(user);

        oauthRepo.save(newAccount);

        return ResponseEntity.ok(new LoginResponse(jwtUtils2.generateToken(user), user.getId(), "Login successful"));
    }

}
