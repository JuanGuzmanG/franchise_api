package com.jjgg.franchise_api.Application.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

public record TopStockProductItemDto(
        @Schema(description = "ID de la sucursal", example = "20")
        Long branchId,
        @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
        String branchName,
        @Schema(description = "ID del producto top", example = "70")
        Long productId,
        @Schema(description = "Nombre del producto top", example = "Producto Top")
        String productName,
        @Schema(description = "Stock del producto top", example = "100")
        Integer stock
) {
}

