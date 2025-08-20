package com.davisiqueira.fraud_guard.config;

import com.davisiqueira.fraud_guard.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(Exception e, HttpServletRequest httpServletRequest) {
        ApiErrorResponse response = buildApiErrorResponse(e, httpServletRequest);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ApiErrorResponse buildApiErrorResponse(Exception e, HttpServletRequest httpServletRequest) {
        return new ApiErrorResponse(
                e.getMessage(),
                httpServletRequest.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                LocalDateTime.now()
        );
    }
}
