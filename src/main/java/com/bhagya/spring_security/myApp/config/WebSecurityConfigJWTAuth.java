package com.bhagya.spring_security.myApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bhagya.spring_security.myApp.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfigJWTAuth {
    
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
         httpSecurity
                 .csrf(csrf->csrf.disable())
                 .sessionManagement(sessionConfig -> 
                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 )
                 .authorizeHttpRequests(auth-> auth
                         .requestMatchers("/auth/**").permitAll()
                         .requestMatchers("/user/**").hasRole("USER")
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         .anyRequest().authenticated()
                 )                
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
         return httpSecurity.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
