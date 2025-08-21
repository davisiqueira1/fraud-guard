package com.davisiqueira.fraud_guard.security;

import com.davisiqueira.fraud_guard.config.SecurityConfiguration;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.service.user.JwtService;
import com.davisiqueira.fraud_guard.service.user.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (endpointIsPublic(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoveryToken(request);
        if (token == null) {
            throw new RuntimeException("Authorization token was not found.");
        }

        String username = jwtService.getSubjectFromToken(token);
        UserModel user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Invalid username as token subject"));

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            return null;
        }

        return authorization.replace("Bearer ", "");
    }

    private boolean endpointIsPublic(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_NO_AUTHENTICATION).contains(requestUri);
    }
}
