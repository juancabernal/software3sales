package com.proveedor.proveedor_mio.controller;

import com.proveedor.proveedor_mio.domain.ProviderStatus;
import com.proveedor.proveedor_mio.dto.ProviderDTO;
import com.proveedor.proveedor_mio.dto.ProviderStatusUpdateDTO;
import com.proveedor.proveedor_mio.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/commercial/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    public Mono<ResponseEntity<ProviderDTO>> createProvider(@RequestBody ProviderDTO request) {
        return providerService.createProvider(request)
            .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }

    @GetMapping("/{providerId}")
    public Mono<ResponseEntity<?>> getProviderById(@PathVariable String providerId) {
        return providerService.getProviderById(providerId)
            .map(this::ok)
            .defaultIfEmpty(notFound(providerId));
    }

    @GetMapping
    public Flux<ProviderDTO> getProviders(@RequestParam(required = false) ProviderStatus status) {
        return providerService.getProviders(status);
    }

    @PutMapping("/{providerId}")
    public Mono<ResponseEntity<?>> updateProvider(@PathVariable String providerId,
                                                  @RequestBody ProviderDTO request) {
        return providerService.getProviderById(providerId)
            .flatMap(current -> {
                if (!current.getEmail().equals(request.getEmail())) {
                    return Mono.just(badRequest("El email no se puede modificar."));
                }
                return providerService.updateProvider(providerId, request)
                    .map(this::ok);
            })
            .defaultIfEmpty(notFound(providerId));
    }

    @PatchMapping("/{providerId}/status")
    public Mono<ResponseEntity<?>> updateStatus(@PathVariable String providerId,
                                                @RequestBody ProviderStatusUpdateDTO request) {
        if (request.getStatus() == null) {
            return Mono.just(badRequest("El estado es obligatorio."));
        }
        return providerService.updateStatus(providerId, request.getStatus())
            .map(this::ok)
            .defaultIfEmpty(notFound(providerId));
    }

    private ResponseEntity<?> ok(Object body) {
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<?> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    private ResponseEntity<?> notFound(String providerId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Provider no encontrado: " + providerId);
    }
}
