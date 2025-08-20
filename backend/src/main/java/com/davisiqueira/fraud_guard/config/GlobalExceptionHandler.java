package com.davisiqueira.fraud_guard.config;

import com.davisiqueira.fraud_guard.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        err -> Optional.ofNullable(err.getDefaultMessage()).orElse("Invalid value"),
                        (err1, err2) -> err1
                ));

        ApiErrorResponse response = buildResponse("Validation error", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintError(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> errors = e.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> extractPath(v.getPropertyPath().toString()),
                        ConstraintViolation::getMessage,
                        (err1, err2) -> err1
                ));

        ApiErrorResponse response = buildResponse("Validation error", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    private ApiErrorResponse buildResponse(String message, HttpServletRequest request, HttpStatus status, Map<String, String> errors) {
        return new ApiErrorResponse(
                message,
                request.getRequestURI(),
                status.value(),
                errors,
                LocalDateTime.now()
        );
    }

    private String extractPath(String path) {
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }
}
