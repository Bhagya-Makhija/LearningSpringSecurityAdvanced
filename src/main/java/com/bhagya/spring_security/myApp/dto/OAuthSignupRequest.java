package com.bhagya.spring_security.myApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthSignupRequest {

    private String email;
    private String username;
    private String password;
    private String providerId;
    private String providerType;
    
}
