package com.codeassessment.ledgerassement.app.excutor.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.annotation.Nullable;

public final class PageableBuilder {

    public static Pageable create(
            @Nullable final Integer pageIndex,
            @Nullable final Integer size,
            @Nullable final String sortColumn,
            @Nullable final String sortDirection) {
        final int pageIndexToUse = pageIndex != null ? pageIndex : 0;
        final int sizeToUse = size != null ? size : 20;
        final String sortColumnToUse = sortColumn != null ? sortColumn : "identifier";
        final Sort.Direction direction = sortDirection != null ? Sort.Direction.valueOf(sortDirection.toUpperCase()) : Sort.Direction.ASC;

        return PageRequest.of(pageIndexToUse, sizeToUse, direction, sortColumnToUse);
    }

}

