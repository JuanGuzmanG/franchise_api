package com.jjgg.franchise_api.Application.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;

public record FranchiseDto(
        @Schema(description = "ID de la franquicia", example = "1")
        Long id,
        @Schema(description = "Nombre de la franquicia", example = "Franquicia Norte")
        String name,
        @Schema(description = "Descripcion de la franquicia", example = "Cobertura en la zona norte")
        String description
) {
}

