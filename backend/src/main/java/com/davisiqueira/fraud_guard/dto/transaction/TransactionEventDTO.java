package com.davisiqueira.fraud_guard.dto.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEventDTO (
        Long id,
        String cpf,
        Instant date,
        BigDecimal value,
        Double lat,
        Double lon,
        Boolean suspect
){
}
