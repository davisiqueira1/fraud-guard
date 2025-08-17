package com.davisiqueira.fraud_guard.dto.transaction;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        String cpf,
        BigDecimal value,
        Float lat,
        Float lon
) {
}
