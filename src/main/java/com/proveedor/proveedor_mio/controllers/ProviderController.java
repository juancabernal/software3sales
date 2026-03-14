package com.proveedor.proveedor_mio.controllers;

import java.util.Map;

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

import com.proveedor.proveedor_mio.dto.ProviderDTO;
import com.proveedor.proveedor_mio.service.ProviderService;

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
    public Mono<ResponseEntity<?>> createProvider(@RequestBody ProviderDTO providerDTO) {
        return providerService.createProvider(providerDTO)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{providerId}")
    public Mono<ProviderDTO> getProviderById(@PathVariable String providerId) {
        return providerService.getProviderById(providerId);
    }

    @GetMapping
    public Flux<ProviderDTO> getProviders(@RequestParam(required = false) String status) {
        return providerService.getProviders(status);
    }

    @PutMapping("/{providerId}")
    public Mono<ResponseEntity<?>> updateProvider(@PathVariable String providerId,
                                                  @RequestBody ProviderDTO providerDTO) {
        return providerService.updateProvider(providerId, providerDTO)
                .map(updated -> ResponseEntity.ok().body(updated));
    }

    @PatchMapping("/{providerId}/status")
    public Mono<ResponseEntity<?>> changeStatus(@PathVariable String providerId,
                                                @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return providerService.changeStatus(providerId, status)
                .map(updated -> ResponseEntity.ok().body(updated));
    }
}
