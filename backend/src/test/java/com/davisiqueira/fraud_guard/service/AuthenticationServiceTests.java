package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.config.SecurityConfiguration;
import com.davisiqueira.fraud_guard.dto.auth.LoginRequestDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginResponseDTO;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.service.auth.AuthenticationService;
import com.davisiqueira.fraud_guard.service.user.JwtService;
import com.davisiqueira.fraud_guard.service.user.UserDetailsImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService service;

    @Captor
    private ArgumentCaptor<UserDetailsImpl> userDetailsCaptor;

    @Nested
    class AuthenticateUser {
        @Nested
        class Sanity {
            @Test
            void shouldReturnToken_whenCredentialsAreValid() {
                final String token = "token";
                LoginRequestDTO request = new LoginRequestDTO("test@mail.com", "password");
                UserDetailsImpl userDetails = new UserDetailsImpl(new UserModel());
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
                when(jwtService.generateToken(any())).thenReturn(token);

                LoginResponseDTO result = service.authenticateUser(request);

                verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
                verify(jwtService).generateToken(userDetails);
                assertEquals(token, result.token());
            }

            @Test
            void authenticateShouldReturnPrincipal() {
                UserDetailsImpl userDetails = new UserDetailsImpl(new UserModel());
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
                LoginRequestDTO request = new LoginRequestDTO("test@mail.com", "password");

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

                service.authenticateUser(request);

                verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
                verify(jwtService).generateToken(userDetailsCaptor.capture());
                assertSame(userDetails, userDetailsCaptor.getValue());
            }

            @Test
            void shouldThrowAuthenticationException_whenAuthenticationFails() {
                LoginRequestDTO request = new LoginRequestDTO("test@mail.com", "password");

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                        .thenThrow(new BadCredentialsException("Bad credentials"));

                assertThrows(BadCredentialsException.class, () -> service.authenticateUser(request));
                verify(jwtService, never()).generateToken(any());
            }
        }
    }
}
