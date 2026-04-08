package com.jjgg.franchise_api.Application.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductDto(
        @Schema(description = "ID del producto", example = "70")
        Long id,
        @Schema(description = "ID de la sucursal dueña", example = "20")
        Long branchId,
        @Schema(description = "Nombre del producto", example = "Producto Top")
        String name,
        @Schema(description = "Stock actual", example = "100")
        Integer stock
) {
}

