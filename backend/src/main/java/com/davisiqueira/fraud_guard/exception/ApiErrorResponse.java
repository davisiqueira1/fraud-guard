package com.davisiqueira.fraud_guard.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse (
        String message,
        String path,
        int statusCode,
        String error,
        LocalDateTime timestamp
) {
}
