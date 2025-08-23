package com.davisiqueira.fraud_guard.dto.auth;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LoginRequestDTO(
        @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid e-mail.")
        String email,

        @Length(min = 8, message = "Your password must have at least 8 characters.")
        String password
) {
}
