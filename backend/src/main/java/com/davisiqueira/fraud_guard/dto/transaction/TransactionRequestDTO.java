package com.davisiqueira.fraud_guard.dto.transaction;

import com.davisiqueira.fraud_guard.validation.validator.cpf.ValidCpf;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @ValidCpf
        String cpf,

        @DecimalMin(value = "0.01", message = "Transaction value must be bigger than 0.")
        BigDecimal value,

        @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90 degrees")
        Double lat,

        @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180 degrees")
        Double lon
) {
}
