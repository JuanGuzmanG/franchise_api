package com.jjgg.franchise_api.Application.dto.query;

public record TopStockProductItemDto(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}

