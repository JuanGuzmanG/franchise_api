package com.jjgg.franchise_api.Application.dto.query;

import java.util.List;

public record TopStockByFranchiseResponseDto(
        Long franchiseId,
        String franchiseName,
        List<TopStockProductItemDto> products
) {
}

