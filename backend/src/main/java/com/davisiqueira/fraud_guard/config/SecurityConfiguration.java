package com.davisiqueira.fraud_guard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    public static final String[] ENDPOINTS_WITH_NO_AUTHENTICATION = {
            "/users/login",
            "/users"
    };
}
