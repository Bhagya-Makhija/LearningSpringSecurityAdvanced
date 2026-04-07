package com.bhagya.spring_security.myApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private Long userId;   
    private String token;   // for JWT authentication
    // we will return a token to the client upon successful login/signup, 
    // which the client can use for subsequent authenticated requests
}
