package com.jjgg.franchise_api.Application.service;

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
import reactor.core.publisher.Mono;

public interface FranchiseService {

    Mono<FranchiseDto> createFranchise(CreateFranchiseRequestDto request);

    Mono<BranchDto> addBranch(Long franchiseId, AddBranchRequestDto request);

    Mono<ProductDto> addProduct(Long branchId, AddProductRequestDto request);

    Mono<Void> deleteProduct(Long branchId, Long productId);

    Mono<ProductDto> updateProductStock(Long productId, UpdateProductStockRequestDto request);

    Mono<TopStockByFranchiseResponseDto> getTopStockByFranchise(Long franchiseId);

    Mono<FranchiseDto> renameFranchise(Long franchiseId, RenameFranchiseRequestDto request);

    Mono<BranchDto> renameBranch(Long branchId, RenameBranchRequestDto request);

    Mono<ProductDto> renameProduct(Long productId, RenameProductRequestDto request);
}

