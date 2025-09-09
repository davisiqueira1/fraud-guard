package com.davisiqueira.fraud_guard.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @Schema(example = "1000")
        @DecimalMin(value = "0.01", message = "Transaction value must be bigger than 0.")
        BigDecimal value,

        @Schema(example = "12.237273")
        @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90 degrees.")
        Double lat,

        @Schema(example = "-0.9801")
        @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180 degrees.")
        Double lon
) {
}
