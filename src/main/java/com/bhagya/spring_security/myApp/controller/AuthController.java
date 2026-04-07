package com.bhagya.spring_security.myApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhagya.spring_security.myApp.dto.LoginRequest;
import com.bhagya.spring_security.myApp.dto.LoginResponse;
import com.bhagya.spring_security.myApp.dto.SignupRequest;
import com.bhagya.spring_security.myApp.dto.SignupResponse;
import com.bhagya.spring_security.myApp.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    // Basic Authentication endpoints - these are currently not used, 
    // but can be enabled by uncommenting the relevant code in WebSecurityConfigBasicAuth and this controller    
    /*
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> basicSignup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.basicSignup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> basicLogin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.basicLogin(request));
    }
    */

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> jwtSignup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.jwtSignup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> jwtLogin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.jwtLogin(request));
    }
}