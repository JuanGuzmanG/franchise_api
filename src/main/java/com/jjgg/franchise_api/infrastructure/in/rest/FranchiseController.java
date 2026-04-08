package com.jjgg.franchise_api.infrastructure.in.rest;

import com.jjgg.franchise_api.Application.dto.command.AddBranchRequestDto;
import com.jjgg.franchise_api.Application.dto.command.AddProductRequestDto;
import com.jjgg.franchise_api.Application.dto.command.CreateFranchiseRequestDto;
import com.jjgg.franchise_api.Application.dto.command.RenameBranchRequestDto;
import com.jjgg.franchise_api.Application.dto.command.RenameFranchiseRequestDto;
import com.jjgg.franchise_api.Application.dto.command.RenameProductRequestDto;
import com.jjgg.franchise_api.Application.dto.command.UpdateProductStockRequestDto;
import com.jjgg.franchise_api.Application.dto.query.TopStockByFranchiseResponseDto;
import com.jjgg.franchise_api.Application.dto.shared.BranchDto;
import com.jjgg.franchise_api.Application.dto.shared.FranchiseDto;
import com.jjgg.franchise_api.Application.dto.shared.ProductDto;
import com.jjgg.franchise_api.Application.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@Tag(name = "Franquicias", description = "Endpoints reactivos para gestionar franquicias, sucursales y productos")
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    //add franchise
    @PostMapping("/franchises")
    @Operation(summary = "Crear franquicia", description = "Crea una franquicia con nombre y descripcion opcional")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Franquicia creada", content = @Content(schema = @Schema(implementation = FranchiseDto.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<FranchiseDto>> createFranchise(@Valid @RequestBody CreateFranchiseRequestDto request) {
        return franchiseService.createFranchise(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    //add branches to franchise
    @PostMapping("/franchises/{franchiseId}/branches")
    @Operation(summary = "Agregar sucursal", description = "Crea una sucursal y la asocia a una franquicia existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sucursal creada", content = @Content(schema = @Schema(implementation = BranchDto.class))),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<BranchDto>> addBranch(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId,
            @Valid @RequestBody AddBranchRequestDto request
    ) {
        return franchiseService.addBranch(franchiseId, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    //add product to branch
    @PostMapping("/branches/{branchId}/products")
    @Operation(summary = "Agregar producto", description = "Crea un producto con stock inicial en una sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<ProductDto>> addProduct(
            @Parameter(description = "ID de la sucursal") @PathVariable Long branchId,
            @Valid @RequestBody AddProductRequestDto request
    ) {
        return franchiseService.addProduct(branchId, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    //delete product
    @DeleteMapping("/branches/{branchId}/products/{productId}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto de una sucursal especifica")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en la sucursal", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<Void>> deleteProduct(
            @Parameter(description = "ID de la sucursal") @PathVariable Long branchId,
            @Parameter(description = "ID del producto") @PathVariable Long productId
    ) {
        return franchiseService.deleteProduct(branchId, productId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    //update stock
    @PatchMapping("/products/{productId}/stock")
    @Operation(summary = "Actualizar stock", description = "Actualiza la cantidad de stock de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<ProductDto>> updateProductStock(
            @Parameter(description = "ID del producto") @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequestDto request
    ) {
        return franchiseService.updateProductStock(productId, request)
                .map(ResponseEntity::ok);
    }

    //get top stock by franchise
    @GetMapping("/franchises/{franchiseId}/top-stock-products")
    @Operation(summary = "Top productos por sucursal", description = "Obtiene, por cada sucursal de una franquicia, el producto con mayor stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = TopStockByFranchiseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<TopStockByFranchiseResponseDto>> getTopStockByFranchise(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId
    ) {
        return franchiseService.getTopStockByFranchise(franchiseId)
                .map(ResponseEntity::ok);
    }

    //change franchise name
    @PatchMapping("/franchises/{franchiseId}/name")
    @Operation(summary = "Renombrar franquicia", description = "Actualiza el nombre de una franquicia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Franquicia actualizada", content = @Content(schema = @Schema(implementation = FranchiseDto.class))),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<FranchiseDto>> renameFranchise(
            @Parameter(description = "ID de la franquicia") @PathVariable Long franchiseId,
            @Valid @RequestBody RenameFranchiseRequestDto request
    ) {
        return franchiseService.renameFranchise(franchiseId, request)
                .map(ResponseEntity::ok);
    }

    //change branch name
    @PatchMapping("/branches/{branchId}/name")
    @Operation(summary = "Renombrar sucursal", description = "Actualiza el nombre de una sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada", content = @Content(schema = @Schema(implementation = BranchDto.class))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<BranchDto>> renameBranch(
            @Parameter(description = "ID de la sucursal") @PathVariable Long branchId,
            @Valid @RequestBody RenameBranchRequestDto request
    ) {
        return franchiseService.renameBranch(branchId, request)
                .map(ResponseEntity::ok);
    }


    //change product name
    @PatchMapping("/products/{productId}/name")
    @Operation(summary = "Renombrar producto", description = "Actualiza el nombre de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado", content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public Mono<ResponseEntity<ProductDto>> renameProduct(
            @Parameter(description = "ID del producto") @PathVariable Long productId,
            @Valid @RequestBody RenameProductRequestDto request
    ) {
        return franchiseService.renameProduct(productId, request)
                .map(ResponseEntity::ok);
    }
}
