package com.jjgg.franchise_api.infrastructure.out.repository;

import com.jjgg.franchise_api.infrastructure.out.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Mono<Product> findByIdAndBranchId(Long id, Long branchId);

    Mono<Product> findFirstByBranchIdOrderByStockDesc(Long branchId);

    Flux<Product> findByBranchId(Long branchId);
}

