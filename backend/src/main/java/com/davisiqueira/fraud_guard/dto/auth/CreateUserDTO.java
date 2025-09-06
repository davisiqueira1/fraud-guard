package com.davisiqueira.fraud_guard.dto.auth;

import com.davisiqueira.fraud_guard.security.RoleName;
import com.davisiqueira.fraud_guard.validation.validator.cpf.ValidCpf;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateUserDTO(
        @ValidCpf
        String cpf,

        @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid e-mail.")
        String email,

        @Length(min = 8, message = "Your password must have at least 8 characters.")
        String password,

        @NotNull(message = "User must have an initial role defined.")
        RoleName role
) {
}
