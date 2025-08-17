package com.davisiqueira.fraud_guard.dto.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(
        Long id,
        String cpf,
        Instant date,
        BigDecimal value,
        Float lat,
        Float lon
) {
}
