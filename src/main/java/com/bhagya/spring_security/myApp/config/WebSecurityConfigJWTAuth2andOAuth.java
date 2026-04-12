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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfigJWTAuth2andOAuth {

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

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}

