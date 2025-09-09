package com.davisiqueira.fraud_guard.dto.auth;

import com.davisiqueira.fraud_guard.security.RoleName;
import com.davisiqueira.fraud_guard.validation.validator.cpf.ValidCpf;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateUserDTO(
        @Schema(example = "99397851004")
        @ValidCpf
        String cpf,

        @Schema(example = "example@mail.com")
        @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid e-mail.")
        String email,

        @Schema(example = "super_secret_password")
        @Length(min = 8, message = "Your password must have at least 8 characters.")
        String password,

        @Schema(example = "ROLE_CUSTOMER")
        @NotNull(message = "User must have an initial role defined.")
        RoleName role
) {
}
