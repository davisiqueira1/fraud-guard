package com.davisiqueira.fraud_guard.dto.transaction;

import java.math.BigDecimal;

public record TransactionsStatisticsDTO(
        BigDecimal average,
        BigDecimal variance,
        BigDecimal standardDeviation,
        BigDecimal minimum,
        BigDecimal maximum,
        Long count
) {
}
