package com.jjgg.franchise_api.infrastructure.in.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ApiErrorResponse(
        @Schema(description = "Fecha/hora del error en UTC")
        Instant timestamp,
        @Schema(description = "Codigo HTTP", example = "404")
        int status,
        @Schema(description = "Nombre del estado HTTP", example = "Not Found")
        String error,
        @Schema(description = "Detalle del error", example = "Franchise not found: 99")
        String message,
        @Schema(description = "Ruta solicitada", example = "/api/franchises/99")
        String path
) {
}

