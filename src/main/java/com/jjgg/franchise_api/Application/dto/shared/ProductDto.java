package com.jjgg.franchise_api.Application.dto.shared;

public record ProductDto(
        Long id,
        Long branchId,
        String name,
        Integer stock
) {
}

