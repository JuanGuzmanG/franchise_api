package com.jjgg.franchise_api.Application.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameFranchiseRequestDto(
        @Schema(description = "Nuevo nombre de la franquicia", example = "Franquicia Premium")
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name
) {
}

