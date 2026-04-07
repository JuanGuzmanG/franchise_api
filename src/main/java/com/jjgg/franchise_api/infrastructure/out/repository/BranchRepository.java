package com.jjgg.franchise_api.infrastructure.out.repository;

import com.jjgg.franchise_api.infrastructure.out.entity.Branch;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchRepository extends ReactiveCrudRepository<Branch, Long> {

	Flux<Branch> findByFranchiseId(Long franchiseId);
}

