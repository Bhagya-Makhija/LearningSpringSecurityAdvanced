package com.bhagya.spring_security.myApp.security;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bhagya.spring_security.myApp.dto.LoginResponse;
import com.bhagya.spring_security.myApp.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
            throws IOException,ServletException {        
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = oauthToken.getPrincipal();
        // System.out.println(user);
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        ResponseEntity<LoginResponse> responseEntity = authService.handleOAuth2LoginRequest(user, registrationId);

        LoginResponse loginResponse = responseEntity.getBody();

        // String token = loginResponse.getToken();

        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json");
        response.getWriter().write(
            new ObjectMapper().writeValueAsString(loginResponse)
        );

        // When frontend is ready
        // Redirect to frontend with token
        // String redirectUrl = "http://localhost:3000/oauth-success?token=" +
        //         URLEncoder.encode(token, StandardCharsets.UTF_8);
        // response.sendRedirect(redirectUrl);

    }
    
}
