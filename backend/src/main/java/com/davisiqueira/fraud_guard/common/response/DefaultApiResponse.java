package com.davisiqueira.fraud_guard.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record DefaultApiResponse<T>(T data, PageInfo pageable) {
    public static <T> DefaultApiResponse<T> of(T data, PageInfo pageable) {
        return new DefaultApiResponse<>(data, pageable);
    }

    public static <T> DefaultApiResponse<T> of(T data) {
        return new DefaultApiResponse<>(data, null);
    }
}
