package com.hotelmanager.model.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> records,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
