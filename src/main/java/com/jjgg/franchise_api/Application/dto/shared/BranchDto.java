package com.jjgg.franchise_api.Application.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;

public record BranchDto(
        @Schema(description = "ID de la sucursal", example = "20")
        Long id,
        @Schema(description = "ID de la franquicia dueña", example = "1")
        Long franchiseId,
        @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
        String name
) {
}

