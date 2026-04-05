package com.bhagya.spring_security.myApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
         httpSecurity
                 .authorizeHttpRequests(auth-> auth
                         .requestMatchers("/public/**").permitAll()
                         .requestMatchers("/user/**").hasRole("USER")
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         .anyRequest().authenticated()
                 )
                 .formLogin(Customizer.withDefaults());
         return httpSecurity.build();

    }
}
