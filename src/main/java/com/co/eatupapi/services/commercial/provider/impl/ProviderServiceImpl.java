package com.co.eatupapi.services.commercial.provider.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.domain.commercial.provider.ProviderStatus;
import com.co.eatupapi.domain.commercial.taxRegime.TaxRegimeStatus;
import com.co.eatupapi.dto.commercial.provider.ProviderDTO;
import com.co.eatupapi.dto.commercial.taxRegime.TaxRegimeDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;
import com.co.eatupapi.repositories.commercial.provider.ProviderRepository;
import com.co.eatupapi.services.commercial.provider.ProviderService;
import com.co.eatupapi.services.commercial.taxRegime.TaxRegimeService;
import com.co.eatupapi.services.inventory.location.LocationService;
import com.co.eatupapi.services.user.CatalogService;
import com.co.eatupapi.utils.commercial.provider.exceptions.BusinessException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ValidationException;
import com.co.eatupapi.utils.commercial.provider.mapper.ProviderMapper;
import com.co.eatupapi.utils.commercial.provider.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final String INVALID_PROVIDER_STATUS_MESSAGE = "Invalid provider status value";

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final CatalogService catalogService;
    private final LocationService locationService;
    private final TaxRegimeService taxRegimeService;

    public ProviderServiceImpl(
            ProviderRepository providerRepository,
            ProviderMapper providerMapper,
            CatalogService catalogService,
            LocationService locationService,
            TaxRegimeService taxRegimeService
    ) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.catalogService = catalogService;
        this.locationService = locationService;
        this.taxRegimeService = taxRegimeService;
    }

    @Override
    public ProviderDTO createProvider(ProviderDTO request) {
        validateProviderPayload(request);
        validateExternalReferences(request);
        validateEmailNotExists(request.getEmail());

        ProviderDomain provider = providerMapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();
        provider.setStatus(ProviderStatus.ACTIVE);
        provider.setCreatedAt(now);
        provider.setModifiedAt(now);

        return providerMapper.toDto(providerRepository.save(provider));
    }

    @Override
    public ProviderDTO getProviderById(String providerId) {
        return providerMapper.toDto(findProviderById(providerId));
    }

    @Override
    public List<ProviderDTO> getProviders(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        List<ProviderDomain> providers = parsedStatus == null
                ? providerRepository.findAll()
                : providerRepository.findByStatus(parsedStatus);

        return providers.stream()
                .sorted(Comparator.comparing(ProviderDomain::getCreatedAt))
                .map(providerMapper::toDto)
                .toList();
    }

    @Override
    public ProviderDTO updateProvider(String providerId, ProviderDTO request) {
        validateProviderPayload(request);
        validateExternalReferences(request);

        ProviderDomain existing = findProviderById(providerId);
        validateImmutableEmail(existing.getEmail(), request.getEmail());

        providerMapper.updateDomain(existing, request);
        existing.setModifiedAt(LocalDateTime.now());

        return providerMapper.toDto(providerRepository.save(existing));
    }

    @Override
    public ProviderDTO updateStatus(String providerId, String status) {
        ProviderStatus newStatus = parseRequiredStatus(status);

        ProviderDomain existing = findProviderById(providerId);
        existing.setStatus(newStatus);
        existing.setModifiedAt(LocalDateTime.now());

        return providerMapper.toDto(providerRepository.save(existing));
    }

    private ProviderDomain findProviderById(String providerId) {
        UUID parsedId = parseUuid(providerId, "Provider not found with id: " + providerId);
        return providerRepository.findById(parsedId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));
    }

    private UUID parseUuid(String value, String notFoundMessage) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(notFoundMessage);
        }
    }

    private ProviderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return ProviderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(INVALID_PROVIDER_STATUS_MESSAGE);
        }
    }

    private ProviderStatus parseRequiredStatus(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException(INVALID_PROVIDER_STATUS_MESSAGE);
        }
        return parsedStatus;
    }

    private void validateProviderPayload(ProviderDTO request) {
        ValidationUtils.requireObject(request, "Provider request cannot be null");
        ValidationUtils.requireText(request.getBusinessName(), "businessName");
        ValidationUtils.requireObject(request.getDocumentTypeId(), "documentTypeId");
        ValidationUtils.requireText(request.getDocumentNumber(), "documentNumber");
        ValidationUtils.requireObject(request.getTaxRegimeId(), "taxRegimeId");
        ValidationUtils.requireText(request.getResponsibleFirstName(), "responsibleFirstName");
        ValidationUtils.requireText(request.getResponsibleLastName(), "responsibleLastName");
        ValidationUtils.requireText(request.getPhone(), "phone");
        ValidationUtils.requireText(request.getEmail(), "email");
        ValidationUtils.requireObject(request.getDepartmentId(), "departmentId");
        ValidationUtils.requireObject(request.getCityId(), "cityId");
        ValidationUtils.requireText(request.getAddress(), "address");
        ValidationUtils.requireObject(request.getBranchId(), "branchId");

        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validatePhone(request.getPhone());
        ValidationUtils.validateNumericValue(request.getDocumentNumber(),
                "Document number must contain only numeric characters");
    }

    private void validateExternalReferences(ProviderDTO request) {
        validateDocumentType(request.getDocumentTypeId());
        validateDepartment(request.getDepartmentId());
        validateCity(request.getCityId(), request.getDepartmentId());
        validateBranch(request.getBranchId());
        validateTaxRegime(request.getTaxRegimeId());
    }

    private void validateDocumentType(Long documentTypeId) {
        UUID documentTypeUuid = toCatalogUuid(documentTypeId);
        if (!catalogService.documentTypeExists(documentTypeUuid)) {
            throw new ResourceNotFoundException("Document type not found or inactive: " + documentTypeId);
        }
    }

    private void validateDepartment(Long departmentId) {
        UUID departmentUuid = toCatalogUuid(departmentId);
        if (!catalogService.departmentExists(departmentUuid)) {
            throw new ResourceNotFoundException("Department not found or inactive: " + departmentId);
        }
    }

    private void validateCity(Long cityId, Long departmentId) {
        UUID cityUuid = toCatalogUuid(cityId);
        UUID departmentUuid = toCatalogUuid(departmentId);

        boolean cityExists = catalogService.cityExists(cityUuid);
        boolean cityBelongsToDepartment = catalogService.getCities(departmentUuid).stream()
                .anyMatch(city -> cityUuid.equals(city.getId()));

        if (!cityExists || !cityBelongsToDepartment) {
            throw new ResourceNotFoundException("City not found or does not belong to department: " + cityId);
        }
    }

    private void validateBranch(Long branchId) {
        LocationResponseDTO branch;
        try {
            branch = locationService.findById(branchId.toString());
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Branch not found or inactive: " + branchId);
        }

        if (!branch.isActive()) {
            throw new BusinessException("Branch not found or inactive: " + branchId);
        }
    }

    private void validateTaxRegime(Long taxRegimeId) {
        String taxRegimeUuid = toCatalogUuid(taxRegimeId).toString();
        TaxRegimeDTO taxRegime;
        try {
            taxRegime = taxRegimeService.getTaxRegimeById(taxRegimeUuid);
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundException("Tax regime not found or inactive: " + taxRegimeId);
        }

        if (taxRegime.getStatus() != TaxRegimeStatus.ACTIVE) {
            throw new BusinessException("Tax regime not found or inactive: " + taxRegimeId);
        }
    }

    private UUID toCatalogUuid(Long id) {
        return new UUID(0L, id);
    }

    private void validateEmailNotExists(String email) {
        providerRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new BusinessException("Email already exists: " + email);
                });
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new BusinessException("Email address cannot be modified once the provider has been created");
        }
    }
}
