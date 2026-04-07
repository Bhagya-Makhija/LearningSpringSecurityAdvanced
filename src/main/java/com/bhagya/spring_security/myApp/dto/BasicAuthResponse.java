package com.bhagya.spring_security.myApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthResponse {
    private String message;
    private Long userId;       
}
