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
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    //add franchise
    @PostMapping("/franchises")
    public Mono<ResponseEntity<FranchiseDto>> createFranchise(@Valid @RequestBody CreateFranchiseRequestDto request) {
        return franchiseService.createFranchise(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    //add branches to franchise
    @PostMapping("/franchises/{franchiseId}/branches")
    public Mono<ResponseEntity<BranchDto>> addBranch(
            @PathVariable Long franchiseId,
            @Valid @RequestBody AddBranchRequestDto request
    ) {
        return franchiseService.addBranch(franchiseId, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    //add product to branch
    @PostMapping("/branches/{branchId}/products")
    public Mono<ResponseEntity<ProductDto>> addProduct(
            @PathVariable Long branchId,
            @Valid @RequestBody AddProductRequestDto request
    ) {
        return franchiseService.addProduct(branchId, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    //delete product
    @DeleteMapping("/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long branchId, @PathVariable Long productId) {
        return franchiseService.deleteProduct(branchId, productId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    //update stock
    @PatchMapping("/products/{productId}/stock")
    public Mono<ResponseEntity<ProductDto>> updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequestDto request
    ) {
        return franchiseService.updateProductStock(productId, request)
                .map(ResponseEntity::ok);
    }

    //get top stock by franchise
    @GetMapping("/franchises/{franchiseId}/top-stock-products")
    public Mono<ResponseEntity<TopStockByFranchiseResponseDto>> getTopStockByFranchise(@PathVariable Long franchiseId) {
        return franchiseService.getTopStockByFranchise(franchiseId)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/franchises/{franchiseId}/name")
    public Mono<ResponseEntity<FranchiseDto>> renameFranchise(
            @PathVariable Long franchiseId,
            @Valid @RequestBody RenameFranchiseRequestDto request
    ) {
        return franchiseService.renameFranchise(franchiseId, request)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/branches/{branchId}/name")
    public Mono<ResponseEntity<BranchDto>> renameBranch(
            @PathVariable Long branchId,
            @Valid @RequestBody RenameBranchRequestDto request
    ) {
        return franchiseService.renameBranch(branchId, request)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/products/{productId}/name")
    public Mono<ResponseEntity<ProductDto>> renameProduct(
            @PathVariable Long productId,
            @Valid @RequestBody RenameProductRequestDto request
    ) {
        return franchiseService.renameProduct(productId, request)
                .map(ResponseEntity::ok);
    }
}
