package com.jjgg.franchise_api.Application.service;

import com.jjgg.franchise_api.Application.dto.command.AddBranchRequestDto;
import com.jjgg.franchise_api.Application.dto.command.AddProductRequestDto;
import com.jjgg.franchise_api.Application.dto.command.CreateFranchiseRequestDto;
import com.jjgg.franchise_api.Application.dto.command.RenameBranchRequestDto;
import com.jjgg.franchise_api.Application.dto.command.RenameFranchiseRequestDto;
import com.jjgg.franchise_api.Application.dto.command.RenameProductRequestDto;
import com.jjgg.franchise_api.Application.dto.command.UpdateProductStockRequestDto;
import com.jjgg.franchise_api.Application.dto.query.TopStockByFranchiseResponseDto;
import com.jjgg.franchise_api.Application.dto.query.TopStockProductItemDto;
import com.jjgg.franchise_api.Application.dto.shared.BranchDto;
import com.jjgg.franchise_api.Application.dto.shared.FranchiseDto;
import com.jjgg.franchise_api.Application.dto.shared.ProductDto;
import com.jjgg.franchise_api.infrastructure.out.entity.Branch;
import com.jjgg.franchise_api.infrastructure.out.entity.Franchise;
import com.jjgg.franchise_api.infrastructure.out.entity.Product;
import com.jjgg.franchise_api.infrastructure.out.repository.BranchRepository;
import com.jjgg.franchise_api.infrastructure.out.repository.FranchiseRepository;
import com.jjgg.franchise_api.infrastructure.out.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public FranchiseServiceImpl(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            ProductRepository productRepository
    ) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<FranchiseDto> createFranchise(CreateFranchiseRequestDto request) {
        Franchise franchise = new Franchise();
        franchise.setName(request.name());
        return franchiseRepository.save(franchise).map(this::toFranchiseDto);
    }

    @Override
    public Mono<BranchDto> addBranch(Long franchiseId, AddBranchRequestDto request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> {
                    Branch branch = new Branch();
                    branch.setName(request.name());
                    branch.setFranchiseId(franchise.getId());
                    return branchRepository.save(branch);
                })
                .map(this::toBranchDto);
    }

    @Override
    public Mono<ProductDto> addProduct(Long branchId, AddProductRequestDto request) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found: " + branchId)))
                .flatMap(branch -> {
                    Product product = new Product();
                    product.setName(request.name());
                    product.setStock(request.stock());
                    product.setBranchId(branch.getId());
                    return productRepository.save(product);
                })
                .map(this::toProductDto);
    }

    @Override
    public Mono<Void> deleteProduct(Long branchId, Long productId) {
        return productRepository.findByIdAndBranchId(productId, branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Product not found: " + productId + " for branch " + branchId
                )))
                .flatMap(productRepository::delete);
    }

    @Override
    public Mono<ProductDto> updateProductStock(Long productId, UpdateProductStockRequestDto request) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found: " + productId)))
                .flatMap(product -> {
                    product.setStock(request.stock());
                    return productRepository.save(product);
                })
                .map(this::toProductDto);
    }

    @Override
    public Mono<TopStockByFranchiseResponseDto> getTopStockByFranchise(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> branchRepository.findByFranchiseId(franchiseId)
                        .flatMap(branch -> productRepository.findFirstByBranchIdOrderByStockDesc(branch.getId())
                                .map(product -> toTopStockItem(branch, product)))
                        .collectList()
                        .map(items -> new TopStockByFranchiseResponseDto(franchise.getId(), franchise.getName(), items))
                );
    }

    @Override
    public Mono<FranchiseDto> renameFranchise(Long franchiseId, RenameFranchiseRequestDto request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> {
                    franchise.setName(request.name());
                    return franchiseRepository.save(franchise);
                })
                .map(this::toFranchiseDto);
    }

    @Override
    public Mono<BranchDto> renameBranch(Long branchId, RenameBranchRequestDto request) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found: " + branchId)))
                .flatMap(branch -> {
                    branch.setName(request.name());
                    return branchRepository.save(branch);
                })
                .map(this::toBranchDto);
    }

    @Override
    public Mono<ProductDto> renameProduct(Long productId, RenameProductRequestDto request) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found: " + productId)))
                .flatMap(product -> {
                    product.setName(request.name());
                    return productRepository.save(product);
                })
                .map(this::toProductDto);
    }

    private TopStockProductItemDto toTopStockItem(Branch branch, Product product) {
        return new TopStockProductItemDto(
                branch.getId(),
                branch.getName(),
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }

    private FranchiseDto toFranchiseDto(Franchise franchise) {
        return new FranchiseDto(franchise.getId(), franchise.getName());
    }

    private BranchDto toBranchDto(Branch branch) {
        return new BranchDto(branch.getId(), branch.getFranchiseId(), branch.getName());
    }

    private ProductDto toProductDto(Product product) {
        return new ProductDto(product.getId(), product.getBranchId(), product.getName(), product.getStock());
    }
}
