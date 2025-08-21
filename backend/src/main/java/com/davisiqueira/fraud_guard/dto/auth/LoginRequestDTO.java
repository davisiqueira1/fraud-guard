package com.davisiqueira.fraud_guard.dto.auth;

public record LoginRequestDTO(
        String email,
        String password
) {
}
