package com.davisiqueira.fraud_guard.validation.validator.cpf;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomCpfValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCpf {
    String message() default "CPF must contain 11 digits and be valid.";
    Class<?>[] groups() default {};
    Class<? super Payload>[] payload() default {};
}
