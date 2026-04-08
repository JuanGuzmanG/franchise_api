package com.jjgg.franchise_api.Application.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TopStockByFranchiseResponseDto(
        @Schema(description = "ID de la franquicia consultada", example = "1")
        Long franchiseId,
        @Schema(description = "Nombre de la franquicia", example = "Franquicia Norte")
        String franchiseName,
        @Schema(description = "Producto con mayor stock por cada sucursal")
        List<TopStockProductItemDto> products
) {
}

