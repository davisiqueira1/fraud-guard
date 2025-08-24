package com.davisiqueira.fraud_guard.config;

import com.davisiqueira.fraud_guard.security.UserAuthenticationFilter;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserAuthenticationFilter userAuthenticationFilter;

    public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    public static final String[] ENDPOINTS_WITH_NO_AUTHENTICATION = {
            "/api/users/login",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
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
                // Registers `UserAuthenticationFilter` to run before `UsernamePasswordAuthenticationFilter` in the filter chain.
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // It's possible to add multiple filters here.
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
