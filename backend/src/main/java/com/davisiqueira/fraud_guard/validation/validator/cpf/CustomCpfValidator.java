package com.davisiqueira.fraud_guard.validation.validator.cpf;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomCpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        return isValidCpf(cpf);
    }

    private boolean isValidCpf(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int d1 = 0, d2 = 0;

            for (int i = 0; i < 9; i++) {
                int digit = cpf.charAt(i) - '0';
                d1 += digit * (10 - i);
                d2 += digit * (11 - i);
            }

            d1 = d1 % 11 < 2 ? 0 : 11 - (d1 % 11);
            d2 += d1 * 2;
            d2 = d2 % 11 < 2 ? 0 : 11 - (d2 % 11);

            return d1 == (cpf.charAt(9) - '0') && d2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}