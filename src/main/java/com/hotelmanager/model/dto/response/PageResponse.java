package com.hotelmanager.model.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> rooms,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
