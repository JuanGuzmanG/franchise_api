package com.jjgg.franchise_api.infrastructure.out.repository;

import com.jjgg.franchise_api.infrastructure.out.entity.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseRepository extends ReactiveCrudRepository<Franchise, Long> {
}

