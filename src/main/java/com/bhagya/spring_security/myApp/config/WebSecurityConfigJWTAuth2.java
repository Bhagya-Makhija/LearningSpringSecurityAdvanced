package com.bhagya.spring_security.myApp.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfigJWTAuth2 {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // to configure application as an OAuth2 Resource Server that accepts and validates JWT tokens 
            // it automates the process of extracting the token from the Authorization header, validating it, and setting up the security context for authenticated requests
            // filter chain will automatically include a JwtAuthenticationFilter that processes incoming requests to validate the JWT token and set the authentication in the security context
             .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) 
                )
            );

        return http.build();
    }

    // STEP 3: Create Key Pair using java.security.KeyPairGenerator
	@Bean
	public KeyPair keyPair() {
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			// 2048 bit RSA encryption; key size. bigger key size - better encryption.
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}				
	}
	
	// STEP 4: Create RSA key object using Key Pair
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey
				.Builder((RSAPublicKey)keyPair.getPublic())
				.privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString())
				.build();
	}
	
	// STEP 6: Create JWKSource (JSON Web Key source) using JWKSet
	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey){
		// STEP 5: create JWKSet using RSA Key
		var jwkSet = new JWKSet(rsaKey);
		/*
		var jwkSource = new JWKSource<SecurityContext>() {
			@Override
			public List get(JWKSelector jwkSelector, SecurityContext context) {
				return jwkSelector.select(jwkSet);
			}
		};
		return jwkSource(rsaKey);
		*/
		return (jwkSelector,context) -> jwkSelector.select(jwkSet);
		
	}
	
	// STEP 7: Use RSA public key for decoding
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder
				.withPublicKey(rsaKey.toRSAPublicKey())
				.build();
	}
	
	// STEP 8: Create Jwt Encoder using JWKSource
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

    // to convert ROLE_USER to USER so that at time of authorization,
    // Spring Security can match the role from JWT with the role required for the endpoint
    // otherwise it will not match because Spring Security by default adds "ROLE_" prefix to the roles defined in the endpoint 
    // and if we have "ROLE_USER" in JWT, it will become "ROLE_ROLE_USER" 
    // and won't match with "ROLE_USER" required for the endpoint
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

        converter.setAuthoritiesClaimName("scope");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setJwtGrantedAuthoritiesConverter(converter);

        return authConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
