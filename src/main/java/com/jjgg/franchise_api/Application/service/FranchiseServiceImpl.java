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
        return franchiseRepository.save(newFranchise(request.name(), request.description())).map(this::toFranchiseDto);
    }

    @Override
    public Mono<BranchDto> addBranch(Long franchiseId, AddBranchRequestDto request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found: " + franchiseId)))
                .flatMap(franchise -> branchRepository.save(newBranch(request.name(), franchise.getId())))
                .map(this::toBranchDto);
    }

    @Override
    public Mono<ProductDto> addProduct(Long branchId, AddProductRequestDto request) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found: " + branchId)))
                .flatMap(branch -> productRepository.save(newProduct(request.name(), request.stock(), branch.getId())))
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
                .flatMap(product -> productRepository.save(copyProductWithStock(product, request.stock())))
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
                .flatMap(franchise -> franchiseRepository.save(copyFranchiseWithName(franchise, request.name())))
                .map(this::toFranchiseDto);
    }

    @Override
    public Mono<BranchDto> renameBranch(Long branchId, RenameBranchRequestDto request) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found: " + branchId)))
                .flatMap(branch -> branchRepository.save(copyBranchWithName(branch, request.name())))
                .map(this::toBranchDto);
    }

    @Override
    public Mono<ProductDto> renameProduct(Long productId, RenameProductRequestDto request) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found: " + productId)))
                .flatMap(product -> productRepository.save(copyProductWithName(product, request.name())))
                .map(this::toProductDto);
    }

    private Franchise newFranchise(String name, String description) {
        Franchise franchise = new Franchise();
        franchise.setName(name);
        franchise.setDescription(description);
        return franchise;
    }

    private Branch newBranch(String name, Long franchiseId) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setFranchiseId(franchiseId);
        return branch;
    }

    private Product newProduct(String name, Integer stock, Long branchId) {
        Product product = new Product();
        product.setName(name);
        product.setStock(stock);
        product.setBranchId(branchId);
        return product;
    }

    private Franchise copyFranchiseWithName(Franchise source, String name) {
        return new Franchise(source.getId(), name, source.getDescription());
    }

    private Branch copyBranchWithName(Branch source, String name) {
        return new Branch(source.getId(), name, source.getFranchiseId());
    }

    private Product copyProductWithName(Product source, String name) {
        Product copy = new Product();
        copy.setId(source.getId());
        copy.setName(name);
        copy.setStock(source.getStock());
        copy.setBranchId(source.getBranchId());
        return copy;
    }

    private Product copyProductWithStock(Product source, Integer stock) {
        Product copy = new Product();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setStock(stock);
        copy.setBranchId(source.getBranchId());
        return copy;
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
        return new FranchiseDto(franchise.getId(), franchise.getName(), franchise.getDescription());
    }

    private BranchDto toBranchDto(Branch branch) {
        return new BranchDto(branch.getId(), branch.getFranchiseId(), branch.getName());
    }

    private ProductDto toProductDto(Product product) {
        return new ProductDto(product.getId(), product.getBranchId(), product.getName(), product.getStock());
    }
}
