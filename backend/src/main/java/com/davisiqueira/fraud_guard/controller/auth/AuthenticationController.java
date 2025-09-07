package com.davisiqueira.fraud_guard.controller.auth;

import com.davisiqueira.fraud_guard.common.error.ApiErrorResponse;
import com.davisiqueira.fraud_guard.common.response.DefaultApiResponse;
import com.davisiqueira.fraud_guard.dto.auth.CreateUserDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginRequestDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginResponseDTO;
import com.davisiqueira.fraud_guard.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Creates an user",
            description = "Creates a new user based on the provided cpf, email and password.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
    })
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserDTO user) {
        service.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "Tries to login based on the provided email and password.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<DefaultApiResponse<LoginResponseDTO>> authenticateUser(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO token = service.authenticateUser(request);

        return new ResponseEntity<>(DefaultApiResponse.of(token), HttpStatus.OK);
    }
}
