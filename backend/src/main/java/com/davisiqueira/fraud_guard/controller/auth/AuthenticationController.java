package com.davisiqueira.fraud_guard.controller.auth;

import com.davisiqueira.fraud_guard.common.response.ApiResponse;
import com.davisiqueira.fraud_guard.dto.auth.CreateUserDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginRequestDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginResponseDTO;
import com.davisiqueira.fraud_guard.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class AuthenticationController {
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserDTO user) {
        service.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> authenticateUser(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO token = service.authenticateUser(request);

        return new ResponseEntity<>(ApiResponse.of(token), HttpStatus.OK);
    }
}
