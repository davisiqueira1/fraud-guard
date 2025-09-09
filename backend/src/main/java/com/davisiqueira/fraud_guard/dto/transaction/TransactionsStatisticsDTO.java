package com.davisiqueira.fraud_guard.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record TransactionsStatisticsDTO(
        @Schema(example = "1000.0")
        BigDecimal average,
        @Schema(example = "0.0")
        BigDecimal variance,
        @Schema(example = "0.0")
        BigDecimal standardDeviation,
        @Schema(example = "1000.0")
        BigDecimal minimum,
        @Schema(example = "1000.0")
        BigDecimal maximum,
        @Schema(example = "2")
        Long count
) {
}
