package com.bhagya.spring_security.myApp.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.bhagya.spring_security.myApp.entity.User;


@Component
public class JwtUtils2 {

    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(User user) {
        var claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(60*10))
                    .subject(user.getUsername())
                    .claim("scope", createScope(user))	// authorities logged in user has
                    .build();
        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);	// initialized JWT encoder in JWTAuthSecurityConfig
        System.out.println("---------------------------------");
        System.out.println(claims.getClaims());
        System.out.println("---------------------------------");
        return jwtEncoder.encode(parameters).getTokenValue();
	}

    private String createScope(User user) {
        System.out.println("---------------------------------");
        System.out.println(user.getRoles());
        System.out.println("---------------------------------");
        return user.getRoles().stream()
                .map(role -> "ROLE_" + role.name()) // IMPORTANT
                .collect(Collectors.joining(" "));
    }
}
