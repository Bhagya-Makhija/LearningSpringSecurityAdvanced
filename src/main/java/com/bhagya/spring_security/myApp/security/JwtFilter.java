package com.bhagya.spring_security.myApp.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bhagya.spring_security.myApp.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{    

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // if no Authorization header is present or it doesn't start with "Bearer ", 
        // skip the jwt filter and continue the chain
        // i.e. to the next filter in the chain which is the UsernamePasswordAuthenticationFilter 
        // that will handle basic authentication if credentials are provided in the request, 
        // otherwise it will return 401 unauthorized response if the endpoint is protected 
        // and doesn't allow anonymous access        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.split("Bearer ")[1].trim();
        String username = jwtUtils.extractUsername(token);

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) 
        {

            var userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.isValid(token, userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // OPTIONAL
                // add additional details about the authentication request such as the remote address and session ID
                // Creates a WebAuthenticationDetails instance that captures request metadata
                //  like the client's IP address, session ID, and other web-specific details from the HttpServletRequest.
                //authToken.setDetails(
                //        new WebAuthenticationDetailsSource().buildDetails(request)
                //);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
