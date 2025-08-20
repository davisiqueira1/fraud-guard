package com.davisiqueira.fraud_guard.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse (
        String message,
        String path,
        int statusCode,
        Map<String, String> errors,
        LocalDateTime timestamp
) {
}
