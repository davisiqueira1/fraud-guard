package com.davisiqueira.fraud_guard.common.response;

import org.springframework.data.domain.Pageable;

public record ApiResponse<T>(T data, Pageable pageable) {
    public static <T> ApiResponse<T> of(T data, Pageable pageable) {
        return new ApiResponse<>(data, pageable);
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, null);
    }
}
