package com.proveedor.proveedor_mio.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.dto.ProviderDTO;
import com.proveedor.proveedor_mio.repository.IProviderRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderService {

    private static final String ACTIVE = "ACTIVE";
    private static final String INACTIVE = "INACTIVE";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    private final IProviderRepository providerRepository;

    public ProviderService(IProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    /**
     * Crea un proveedor nuevo aplicando validaciones y valores por defecto.
     */
    public Mono<ProviderDTO> createProvider(ProviderDTO providerDTO) {
        validateRequiredFields(providerDTO);
        validateFormats(providerDTO);

        return providerRepository.findByEmail(providerDTO.getEmail())
                .flatMap(existing -> Mono.<ProviderDTO>error(new ResponseStatusException(
                        HttpStatus.CONFLICT, "El email ya se encuentra registrado")))
                .switchIfEmpty(Mono.defer(() -> {
                    Provider provider = toEntity(providerDTO);
                    provider.setId(UUID.randomUUID().toString());
                    provider.setStatus(ACTIVE);
                    provider.setCreatedDate(LocalDateTime.now());
                    provider.setModifiedDate(LocalDateTime.now());

                    return providerRepository.save(provider).map(this::toDto);
                }));
    }

    /**
     * Obtiene un proveedor por id.
     */
    public Mono<ProviderDTO> getProviderById(String providerId) {
        return providerRepository.findById(providerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado")))
                .map(this::toDto);
    }

    /**
     * Lista proveedores opcionalmente filtrando por estado.
     */
    public Flux<ProviderDTO> getProviders(String status) {
        if (status == null || status.isBlank()) {
            return providerRepository.findAll().map(this::toDto);
        }

        String normalizedStatus = status.toUpperCase();
        validateStatus(normalizedStatus);
        return providerRepository.findByStatus(normalizedStatus).map(this::toDto);
    }

    /**
     * Actualiza un proveedor respetando la inmutabilidad del email.
     */
    public Mono<ProviderDTO> updateProvider(String providerId, ProviderDTO providerDTO) {
        validateRequiredFields(providerDTO);
        validateFormats(providerDTO);

        return providerRepository.findById(providerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado")))
                .flatMap(existing -> {
                    if (!existing.getEmail().equalsIgnoreCase(providerDTO.getEmail())) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "El email no se puede modificar"));
                    }

                    existing.setBusinessName(providerDTO.getBusinessName());
                    existing.setDocumentTypeId(providerDTO.getDocumentTypeId());
                    existing.setDocumentNumber(providerDTO.getDocumentNumber());
                    existing.setTaxRegimeId(providerDTO.getTaxRegimeId());
                    existing.setResponsibleFirstName(providerDTO.getResponsibleFirstName());
                    existing.setResponsibleLastName(providerDTO.getResponsibleLastName());
                    existing.setPhone(providerDTO.getPhone());
                    existing.setDepartmentId(providerDTO.getDepartmentId());
                    existing.setCityId(providerDTO.getCityId());
                    existing.setAddress(providerDTO.getAddress());
                    existing.setBranchId(providerDTO.getBranchId());
                    existing.setModifiedDate(LocalDateTime.now());

                    return providerRepository.save(existing).map(this::toDto);
                });
    }

    /**
     * Actualiza estado del proveedor (soft delete).
     */
    public Mono<ProviderDTO> changeStatus(String providerId, String status) {
        if (status == null || status.isBlank()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado requerido"));
        }

        String normalizedStatus = status.toUpperCase();
        validateStatus(normalizedStatus);

        return providerRepository.findById(providerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado")))
                .flatMap(provider -> {
                    provider.setStatus(normalizedStatus);
                    provider.setModifiedDate(LocalDateTime.now());
                    return providerRepository.save(provider).map(this::toDto);
                });
    }

    private void validateRequiredFields(ProviderDTO providerDTO) {
        if (isBlank(providerDTO.getBusinessName())
                || isBlank(providerDTO.getDocumentTypeId())
                || isBlank(providerDTO.getDocumentNumber())
                || isBlank(providerDTO.getTaxRegimeId())
                || isBlank(providerDTO.getResponsibleFirstName())
                || isBlank(providerDTO.getResponsibleLastName())
                || isBlank(providerDTO.getPhone())
                || isBlank(providerDTO.getEmail())
                || isBlank(providerDTO.getDepartmentId())
                || isBlank(providerDTO.getCityId())
                || isBlank(providerDTO.getAddress())
                || isBlank(providerDTO.getBranchId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los campos obligatorios deben tener valor");
        }
    }

    private void validateFormats(ProviderDTO providerDTO) {
        if (!EMAIL_PATTERN.matcher(providerDTO.getEmail()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato del email no es válido");
        }

        if (!NUMERIC_PATTERN.matcher(providerDTO.getPhone()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono solo debe contener números");
        }

        if (!NUMERIC_PATTERN.matcher(providerDTO.getDocumentNumber()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de documento solo debe contener números");
        }
    }

    private void validateStatus(String status) {
        if (!ACTIVE.equals(status) && !INACTIVE.equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido. Use ACTIVE o INACTIVE");
        }
    }

    private ProviderDTO toDto(Provider provider) {
        ProviderDTO dto = new ProviderDTO();
        dto.setId(provider.getId());
        dto.setBusinessName(provider.getBusinessName());
        dto.setDocumentTypeId(provider.getDocumentTypeId());
        dto.setDocumentType(resolveCatalogLabel(provider.getDocumentTypeId()));
        dto.setDocumentNumber(provider.getDocumentNumber());
        dto.setTaxRegimeId(provider.getTaxRegimeId());
        dto.setTaxRegime(resolveCatalogLabel(provider.getTaxRegimeId()));
        dto.setResponsibleFirstName(provider.getResponsibleFirstName());
        dto.setResponsibleLastName(provider.getResponsibleLastName());
        dto.setPhone(provider.getPhone());
        dto.setEmail(provider.getEmail());
        dto.setDepartmentId(provider.getDepartmentId());
        dto.setDepartment(resolveCatalogLabel(provider.getDepartmentId()));
        dto.setCityId(provider.getCityId());
        dto.setCity(resolveCatalogLabel(provider.getCityId()));
        dto.setAddress(provider.getAddress());
        dto.setBranchId(provider.getBranchId());
        dto.setBranch(resolveCatalogLabel(provider.getBranchId()));
        dto.setStatus(provider.getStatus());
        dto.setCreatedDate(provider.getCreatedDate());
        dto.setModifiedDate(provider.getModifiedDate());
        return dto;
    }

    private Provider toEntity(ProviderDTO dto) {
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

    private String resolveCatalogLabel(String id) {
        if (id == null) {
            return null;
        }

        Map<String, String> sampleCatalog = Map.of();
        return sampleCatalog.getOrDefault(id, id);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
