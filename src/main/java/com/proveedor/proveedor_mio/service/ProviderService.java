package com.proveedor.proveedor_mio.service;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import com.proveedor.proveedor_mio.dto.ProviderDTO;
import com.proveedor.proveedor_mio.repository.ProviderRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    private final List<Provider> providers = new ArrayList<>();

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @PostConstruct
    public void initData() {
        providers.clear();
        providers.addAll(providerRepository.loadInitialProviders());
    }

    public Mono<ProviderDTO> createProvider(ProviderDTO request) {
        Provider provider = toDomain(request);
        provider.setId(UUID.randomUUID().toString());
        provider.setStatus(ProviderStatus.ACTIVE);
        provider.setCreatedDate(LocalDateTime.now());
        provider.setModifiedDate(LocalDateTime.now());
        providers.add(provider);
        return Mono.just(toDto(provider));
    }

    public Mono<ProviderDTO> getProviderById(String providerId) {
        return Flux.fromIterable(providers)
            .filter(provider -> provider.getId().equals(providerId))
            .next()
            .map(this::toDto);
    }

    public Flux<ProviderDTO> getProviders(ProviderStatus status) {
        Flux<Provider> source = Flux.fromIterable(providers);
        if (status != null) {
            source = source.filter(provider -> provider.getStatus() == status);
        }
        return source.map(this::toDto);
    }

    public Mono<ProviderDTO> updateProvider(String providerId, ProviderDTO request) {
        return Flux.fromIterable(providers)
            .filter(provider -> provider.getId().equals(providerId))
            .next()
            .map(existing -> {
                existing.setBusinessName(request.getBusinessName());
                existing.setDocumentTypeId(request.getDocumentTypeId());
                existing.setDocumentNumber(request.getDocumentNumber());
                existing.setTaxRegimeId(request.getTaxRegimeId());
                existing.setResponsibleFirstName(request.getResponsibleFirstName());
                existing.setResponsibleLastName(request.getResponsibleLastName());
                existing.setPhone(request.getPhone());
                existing.setDepartmentId(request.getDepartmentId());
                existing.setCityId(request.getCityId());
                existing.setAddress(request.getAddress());
                existing.setBranchId(request.getBranchId());
                existing.setModifiedDate(LocalDateTime.now());
                return toDto(existing);
            });
    }

    public Mono<ProviderDTO> updateStatus(String providerId, ProviderStatus newStatus) {
        return Flux.fromIterable(providers)
            .filter(provider -> provider.getId().equals(providerId))
            .next()
            .map(existing -> {
                existing.setStatus(newStatus);
                existing.setModifiedDate(LocalDateTime.now());
                return toDto(existing);
            });
    }

    private ProviderDTO toDto(Provider provider) {
        ProviderDTO dto = new ProviderDTO();
        dto.setId(provider.getId());
        dto.setBusinessName(provider.getBusinessName());
        dto.setDocumentTypeId(provider.getDocumentTypeId());
        dto.setDocumentNumber(provider.getDocumentNumber());
        dto.setTaxRegimeId(provider.getTaxRegimeId());
        dto.setResponsibleFirstName(provider.getResponsibleFirstName());
        dto.setResponsibleLastName(provider.getResponsibleLastName());
        dto.setPhone(provider.getPhone());
        dto.setEmail(provider.getEmail());
        dto.setDepartmentId(provider.getDepartmentId());
        dto.setCityId(provider.getCityId());
        dto.setAddress(provider.getAddress());
        dto.setBranchId(provider.getBranchId());
        dto.setStatus(provider.getStatus());
        dto.setCreatedDate(provider.getCreatedDate());
        dto.setModifiedDate(provider.getModifiedDate());
        return dto;
    }

    private Provider toDomain(ProviderDTO dto) {
        Provider provider = new Provider();
        provider.setBusinessName(dto.getBusinessName());
        provider.setDocumentTypeId(dto.getDocumentTypeId());
        provider.setDocumentNumber(dto.getDocumentNumber());
        provider.setTaxRegimeId(dto.getTaxRegimeId());
        provider.setResponsibleFirstName(dto.getResponsibleFirstName());
        provider.setResponsibleLastName(dto.getResponsibleLastName());
        provider.setPhone(dto.getPhone());
        provider.setEmail(dto.getEmail());
        provider.setDepartmentId(dto.getDepartmentId());
        provider.setCityId(dto.getCityId());
        provider.setAddress(dto.getAddress());
        provider.setBranchId(dto.getBranchId());
        return provider;
    }
}
