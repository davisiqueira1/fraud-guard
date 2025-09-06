package com.davisiqueira.fraud_guard.common.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public record PageInfo(
        int pageNumber,
        int pageSize,
        long offset,
        boolean first,
        boolean last,
        boolean empty,
        Sort sort
) {
    public static PageInfo from(Page<?> pageable) {
        return new PageInfo(
                pageable.getPageable().getPageNumber(),
                pageable.getPageable().getPageSize(),
                pageable.getPageable().getOffset(),
                pageable.isFirst(),
                pageable.isLast(),
                pageable.isEmpty(),
                pageable.getSort()
        );
    }
}
