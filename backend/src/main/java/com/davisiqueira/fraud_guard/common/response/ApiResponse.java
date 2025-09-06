package com.davisiqueira.fraud_guard.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiResponse<T>(T data, PageInfo pageable) {
    public static <T> ApiResponse<T> of(T data, PageInfo pageable) {
        return new ApiResponse<>(data, pageable);
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, null);
    }
}
