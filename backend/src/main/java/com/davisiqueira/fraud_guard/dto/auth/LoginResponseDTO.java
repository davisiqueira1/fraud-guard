package com.davisiqueira.fraud_guard.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
        @Schema(example = "<header>.<payload>.<signature>")
        String token
) {
}
