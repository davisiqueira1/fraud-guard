package com.davisiqueira.fraud_guard.dto.auth;

import com.davisiqueira.fraud_guard.security.RoleName;

public record CreateUserDTO(
        String email,
        String password,
        RoleName role
) {
}
