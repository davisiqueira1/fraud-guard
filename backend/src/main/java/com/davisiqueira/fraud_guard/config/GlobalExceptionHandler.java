package com.davisiqueira.fraud_guard.config;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.davisiqueira.fraud_guard.exception.ApiErrorResponse;
import com.davisiqueira.fraud_guard.exception.MissingCredentialsException;
import com.davisiqueira.fraud_guard.exception.TransactionNotFoundException;
import com.davisiqueira.fraud_guard.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameNotFound(UsernameNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        Map<String, String> errors = Map.ofEntries(
                Map.entry(e.getClass().getSimpleName(), e.getMessage())
        );

        ApiErrorResponse response = buildResponse("Authentication error", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ApiErrorResponse> handleJwtVerification(JWTVerificationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        Map<String, String> errors = Map.ofEntries(
                Map.entry(e.getClass().getSimpleName(), e.getMessage())
        );

        ApiErrorResponse response = buildResponse("Invalid or expired JWT token", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<ApiErrorResponse> handleJwtCreation(JWTVerificationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        Map<String, String> errors = Map.ofEntries(
                Map.entry(e.getClass().getSimpleName(), e.getMessage())
        );

        ApiErrorResponse response = buildResponse("Failed to generate JWT token", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MissingCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingCredentials(MissingCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        Map<String, String> errors = Map.ofEntries(
                Map.entry(e.getClass().getSimpleName(), e.getMessage())
        );

        ApiErrorResponse response = buildResponse("Failed to authenticate user", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        Map<String, String> errors = Map.ofEntries(
                Map.entry(e.getClass().getSimpleName(), e.getMessage())
        );

        ApiErrorResponse response = buildResponse("User not found with the provided credentials", request, status, errors);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTransactionNotFound(TransactionNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        Map<String, String> errors = Map.ofEntries(
                Map.entry(e.getClass().getSimpleName(), e.getMessage())
        );

        ApiErrorResponse response = buildResponse("Transaction not found with the provided identification", request, status, errors);

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
