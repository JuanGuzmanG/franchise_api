package com.jjgg.franchise_api.Application.service;

import com.jjgg.franchise_api.Application.dto.command.AddBranchRequestDto;
import com.jjgg.franchise_api.Application.dto.command.AddProductRequestDto;
import com.jjgg.franchise_api.Application.dto.command.CreateFranchiseRequestDto;
import com.jjgg.franchise_api.Application.dto.command.UpdateProductStockRequestDto;
import com.jjgg.franchise_api.infrastructure.out.entity.Branch;
import com.jjgg.franchise_api.infrastructure.out.entity.Franchise;
import com.jjgg.franchise_api.infrastructure.out.entity.Product;
import com.jjgg.franchise_api.infrastructure.out.repository.BranchRepository;
import com.jjgg.franchise_api.infrastructure.out.repository.FranchiseRepository;
import com.jjgg.franchise_api.infrastructure.out.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FranchiseServiceImpl service;

    @Test
    void createFranchiseShouldReturnCreatedDto() {
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> {
            Franchise toSave = invocation.getArgument(0);
            toSave.setId(1L);
            return Mono.just(toSave);
        });

        StepVerifier.create(service.createFranchise(new CreateFranchiseRequestDto("Franquicia A")))
                .expectNextMatches(dto -> dto.id().equals(1L) && dto.name().equals("Franquicia A"))
                .verifyComplete();

        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void addBranchShouldReturnCreatedDto() {
        Franchise franchise = new Franchise(10L, "Franquicia A");
        Branch savedBranch = new Branch(20L, "Sucursal Norte", 10L);

        when(franchiseRepository.findById(10L)).thenReturn(Mono.just(franchise));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(savedBranch));

        StepVerifier.create(service.addBranch(10L, new AddBranchRequestDto("Sucursal Norte")))
                .expectNextMatches(dto -> dto.id().equals(20L)
                        && dto.franchiseId().equals(10L)
                        && dto.name().equals("Sucursal Norte"))
                .verifyComplete();

        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void addBranchShouldEmitNotFoundWhenFranchiseDoesNotExist() {
        when(franchiseRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.addBranch(99L, new AddBranchRequestDto("Sucursal X")))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(ResourceNotFoundException.class, error);
                    assertTrue(error.getMessage().contains("Franchise not found: 99"));
                })
                .verify();

        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    void addProductShouldReturnCreatedDto() {
        Branch branch = new Branch(30L, "Sucursal Centro", 10L);
        Product saved = new Product(40L, "Producto A", 15, 30L);

        when(branchRepository.findById(30L)).thenReturn(Mono.just(branch));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.addProduct(30L, new AddProductRequestDto("Producto A", 15)))
                .expectNextMatches(dto -> dto.id().equals(40L)
                        && dto.branchId().equals(30L)
                        && dto.stock().equals(15))
                .verifyComplete();

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void addProductShouldEmitNotFoundWhenBranchDoesNotExist() {
        when(branchRepository.findById(55L)).thenReturn(Mono.empty());

        StepVerifier.create(service.addProduct(55L, new AddProductRequestDto("Producto X", 5)))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(ResourceNotFoundException.class, error);
                    assertTrue(error.getMessage().contains("Branch not found: 55"));
                })
                .verify();

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProductStockShouldUpdateAndReturnDto() {
        Product existing = new Product(70L, "Producto Stock", 4, 30L);
        Product updated = new Product(70L, "Producto Stock", 20, 30L);

        when(productRepository.findById(70L)).thenReturn(Mono.just(existing));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(service.updateProductStock(70L, new UpdateProductStockRequestDto(20)))
                .expectNextMatches(dto -> dto.id().equals(70L) && dto.stock().equals(20))
                .verifyComplete();
    }

    @Test
    void updateProductStockShouldEmitNotFoundWhenProductDoesNotExist() {
        when(productRepository.findById(88L)).thenReturn(Mono.empty());

        StepVerifier.create(service.updateProductStock(88L, new UpdateProductStockRequestDto(20)))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(ResourceNotFoundException.class, error);
                    assertTrue(error.getMessage().contains("Product not found: 88"));
                })
                .verify();
    }
}
