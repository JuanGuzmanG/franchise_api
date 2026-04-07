package com.jjgg.franchise_api.Application.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddProductRequestDto(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,
        @NotNull(message = "stock is required")
        @Min(value = 0, message = "stock must be >= 0")
        Integer stock
) {
}

