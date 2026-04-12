package com.bhagya.spring_security.myApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.bhagya.spring_security.myApp.security.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2FilterChainConfig {
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // to configure application as an OAuth2 Resource Server that accepts and validates JWT tokens 
            // it automates the process of extracting the token from the Authorization header, validating it, and setting up the security context for authenticated requests
            // filter chain will automatically include a JwtAuthenticationFilter that processes incoming requests to validate the JWT token and set the authentication in the security context
			.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) 
                )
            )
            .oauth2Login(oauth2 -> oauth2
				.failureHandler((req,res,exc)->{
					log.error("OAuth2 login failed: "+ exc.getMessage());
				})
				// .failureHandler(
				// new AuthenticationFailureHandler() {
				// 	@Override
				// 	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				// 			AuthenticationException exception) throws IOException, ServletException {
				// 		log.error("OAuth2 login failed: " + exception.getMessage());
				// 		// response.sendRedirect("/login?error");
				// 	}					
				// })
				.successHandler(oAuth2SuccessHandler)
			);
			

        return http.build();
    }

    // to convert ROLE_USER to USER so that at time of authorization,
    // Spring Security can match the role from JWT with the role required for the endpoint
    // otherwise it will not match because Spring Security by default adds "ROLE_" prefix to the roles defined in the endpoint 
    // and if we have "ROLE_USER" in JWT, it will become "ROLE_ROLE_USER" 
    // and won't match with "ROLE_USER" required for the endpoint
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

        converter.setAuthoritiesClaimName("scope");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setJwtGrantedAuthoritiesConverter(converter);

        return authConverter;
    }
}
