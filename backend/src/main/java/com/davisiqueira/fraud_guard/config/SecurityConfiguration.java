package com.davisiqueira.fraud_guard.config;

import com.davisiqueira.fraud_guard.common.error.ApiErrorResponse;
import com.davisiqueira.fraud_guard.security.UserAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserAuthenticationFilter userAuthenticationFilter;
    private final ObjectMapper objectMapper;


    public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter, ObjectMapper objectMapper) {
        this.userAuthenticationFilter = userAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    public static final String[] ENDPOINTS_WITH_NO_AUTHENTICATION = {
            "/api/users/login",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Allows unauthenticated POST requests to /users.
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(ENDPOINTS_WITH_NO_AUTHENTICATION).permitAll()
                        // Requires ROLE_ADMIN for any request to /api/users or /api/users/** that didn't match earlier rules.
                        // Note: only POST /api/users (exact) is public; POST /api/users/** still requires ADMIN.
                        .requestMatchers("/api/users", "/api/users/**").hasRole("ADMINISTRATOR")
                        .requestMatchers("/api/admin", "/api/admin/**").hasRole("ADMINISTRATOR")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                // Registers `UserAuthenticationFilter` to run before `UsernamePasswordAuthenticationFilter` in the filter chain.
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // It's possible to add multiple filters here.
                .build();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            final int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            response.setStatus(statusCode);
            response.setContentType("application/json;charset=UTF-8");
            ApiErrorResponse body = new ApiErrorResponse(
                    "Missing or invalid token.",
                    request.getRequestURI(),
                    statusCode,
                    Map.ofEntries(
                            Map.entry(authException.getClass().getSimpleName(), authException.getMessage())
                    ),
                    LocalDateTime.now()
            );
            response.getWriter().write(objectToJson(body));
        };
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            final int statusCode = HttpServletResponse.SC_FORBIDDEN;
            response.setStatus(statusCode);
            response.setContentType("application/json;charset=UTF-8");
            ApiErrorResponse body = new ApiErrorResponse(
                    "Insufficient permission: ROLE_ADMINISTRATOR is required.",
                    request.getRequestURI(),
                    statusCode,
                    Map.ofEntries(
                            Map.entry(accessDeniedException.getClass().getSimpleName(), accessDeniedException.getMessage())
                    ),
                    LocalDateTime.now()
            );
            response.getWriter().write(objectToJson(body));
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private String objectToJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            return "{\"error\":\"serialization error\"}";
        }
    }
}
