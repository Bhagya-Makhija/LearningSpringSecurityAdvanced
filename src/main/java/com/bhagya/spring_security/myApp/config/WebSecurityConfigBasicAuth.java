package com.bhagya.spring_security.myApp.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Commented annotations to disable this configuration 
// @Configuration
public class WebSecurityConfigBasicAuth {
    // @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
         httpSecurity
                 .csrf(csrf->csrf.disable())
                 .authorizeHttpRequests(auth-> auth
                         .requestMatchers("/auth/**").permitAll()
                         .requestMatchers("/user/**").hasRole("USER")
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         .anyRequest().authenticated()
                 )
                //  .formLogin(Customizer.withDefaults());
                .httpBasic(Customizer.withDefaults());
         return httpSecurity.build();

    }

    // @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
