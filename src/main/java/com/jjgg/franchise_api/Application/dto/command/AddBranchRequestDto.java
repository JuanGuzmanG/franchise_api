package com.jjgg.franchise_api.Application.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddBranchRequestDto(
        @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name
) {
}

