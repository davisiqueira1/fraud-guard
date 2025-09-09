package com.davisiqueira.fraud_guard.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LoginRequestDTO(
        @Schema(example = "example@mail.com")
        @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid e-mail.")
        String email,

        @Schema(example = "super_secret_password")
        @Length(min = 8, message = "Your password must have at least 8 characters.")
        String password
) {
}
