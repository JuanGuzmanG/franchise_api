package com.jjgg.franchise_api.Application.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddProductRequestDto(
        @Schema(description = "Nombre del producto", example = "Hamburguesa Clasica")
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,
        @Schema(description = "Stock inicial del producto", example = "25")
        @NotNull(message = "stock is required")
        @Min(value = 0, message = "stock must be >= 0")
        Integer stock
) {
}

