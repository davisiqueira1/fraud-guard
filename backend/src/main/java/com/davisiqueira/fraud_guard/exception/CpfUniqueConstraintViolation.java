package com.davisiqueira.fraud_guard.exception;

public class CpfUniqueConstraintViolation extends RuntimeException {
    public CpfUniqueConstraintViolation(String message) {
        super(message);
    }
}
