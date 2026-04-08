package com.jjgg.franchise_api.Application.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStockRequestDto(
        @Schema(description = "Nuevo stock del producto", example = "50")
        @NotNull(message = "stock is required")
        @Min(value = 0, message = "stock must be >= 0")
        Integer stock
) {
}

