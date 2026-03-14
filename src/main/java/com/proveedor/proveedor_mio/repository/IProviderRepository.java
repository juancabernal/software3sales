package com.proveedor.proveedor_mio.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proveedor.proveedor_mio.domain.Provider;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProviderRepository extends ReactiveCrudRepository<Provider, String> {

    Flux<Provider> findByStatus(String status);

    Mono<Provider> findByEmail(String email);
}
