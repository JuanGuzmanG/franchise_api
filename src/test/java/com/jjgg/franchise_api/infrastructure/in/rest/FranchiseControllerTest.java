package com.jjgg.franchise_api.infrastructure.in.rest;

import com.jjgg.franchise_api.Application.dto.query.TopStockByFranchiseResponseDto;
import com.jjgg.franchise_api.Application.dto.query.TopStockProductItemDto;
import com.jjgg.franchise_api.Application.dto.shared.BranchDto;
import com.jjgg.franchise_api.Application.dto.shared.FranchiseDto;
import com.jjgg.franchise_api.Application.dto.shared.ProductDto;
import com.jjgg.franchise_api.Application.service.FranchiseService;
import com.jjgg.franchise_api.Application.service.ResourceNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FranchiseControllerTest {

    private FranchiseService franchiseService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        franchiseService = mock(FranchiseService.class);
        FranchiseController controller = new FranchiseController(franchiseService);
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createFranchiseShouldReturnCreatedStatus() {
        when(franchiseService.createFranchise(any()))
                .thenReturn(Mono.just(new FranchiseDto(1L, "Franquicia A")));

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Franquicia A\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Franquicia A");
    }

    @Test
    void addBranchShouldReturnNotFoundWhenFranchiseDoesNotExist() {
        when(franchiseService.addBranch(eq(99L), any()))
                .thenReturn(Mono.error(new ResourceNotFoundException("Franchise not found: 99")));

        webTestClient.post()
                .uri("/api/franchises/99/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Sucursal X\"}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found")
                .jsonPath("$.message").isEqualTo("Franchise not found: 99");
    }

    @Test
    void addBranchShouldReturnCreatedStatus() {
        when(franchiseService.addBranch(eq(10L), any()))
                .thenReturn(Mono.just(new BranchDto(20L, 10L, "Sucursal Norte")));

        webTestClient.post()
                .uri("/api/franchises/10/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Sucursal Norte\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(20)
                .jsonPath("$.franchiseId").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("Sucursal Norte");
    }

    @Test
    void updateProductStockShouldReturnOk() {
        when(franchiseService.updateProductStock(eq(77L), any()))
                .thenReturn(Mono.just(new ProductDto(77L, 30L, "Producto A", 25)));

        webTestClient.patch()
                .uri("/api/products/77/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"stock\":25}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(77)
                .jsonPath("$.stock").isEqualTo(25);
    }

    @Test
    void deleteProductShouldReturnNoContent() {
        when(franchiseService.deleteProduct(30L, 77L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/branches/30/products/77")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void getTopStockByFranchiseShouldReturnOk() {
        TopStockProductItemDto item = new TopStockProductItemDto(20L, "Sucursal Norte", 70L, "Producto Top", 100);
        TopStockByFranchiseResponseDto response = new TopStockByFranchiseResponseDto(10L, "Franquicia A", List.of(item));

        when(franchiseService.getTopStockByFranchise(10L)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/franchises/10/top-stock-products")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.franchiseId").isEqualTo(10)
                .jsonPath("$.products[0].branchName").isEqualTo("Sucursal Norte")
                .jsonPath("$.products[0].stock").isEqualTo(100);
    }
}
