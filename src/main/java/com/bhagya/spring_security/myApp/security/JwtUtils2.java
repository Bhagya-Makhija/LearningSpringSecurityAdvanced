package com.bhagya.spring_security.myApp.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.bhagya.spring_security.myApp.entity.AuthProviderType;
import com.bhagya.spring_security.myApp.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils2 {

    private final @Lazy JwtEncoder jwtEncoder;

    public String generateToken(User user) {
        var claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(60*10))
                    .subject(user.getUsername())
                    .claim("scope", createScope(user))	// authorities logged in user has
                    .build();
        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);	// initialized JWT encoder in JWTAuthSecurityConfig

        return jwtEncoder.encode(parameters).getTokenValue();
	}

    private String createScope(User user) {        
        return user.getRoles().stream()
                .map(role -> "ROLE_" + role.name()) // IMPORTANT
                .collect(Collectors.joining(" "));
    }

    // for OAuth2 
    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId){
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 Provider: " + registrationId);
        };
    }

    @SuppressWarnings("null")
    public String determineProviderIdFromOAuth2User(OAuth2User oauth2User, String registrationId) {
        String providerId = switch(registrationId.toLowerCase()) {
            case "google" -> oauth2User.getAttribute("sub");
            case "github" -> (oauth2User.getAttribute("id") != null) ? oauth2User.getAttribute("id").toString() : null;
            default -> {
                log.error("Unsupported OAuth2 Provider: {}", registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 Provider: " + registrationId);
            }
        };

        if(providerId == null || providerId.isBlank()) {
            log.error("Provider ID is null for OAuth2 Provider: {}", registrationId);
            throw new IllegalArgumentException("Provider ID is null for OAuth2 Provider: " + registrationId);
        }
        return providerId;
    }

    public String determineEmailFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId) {

        String email = oAuth2User.getAttribute("email");
        if(email != null && !email.isBlank()) {
            return email;
        }

        String username = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };
        return username;
    }
}
