package com.davisiqueira.fraud_guard.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "99397851004")
        String cpf,
        @Schema(example = "1970-01-01T00:00:00Z")
        Instant date,
        @Schema(example = "1000")
        BigDecimal value,
        @Schema(example = "12.237273")
        Double lat,
        @Schema(example = "-0.9801")
        Double lon
) {
}
