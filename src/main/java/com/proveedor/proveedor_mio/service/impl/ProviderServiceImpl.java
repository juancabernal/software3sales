package com.proveedor.proveedor_mio.service.impl;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import com.proveedor.proveedor_mio.dto.ProviderDTO;
import com.proveedor.proveedor_mio.repository.ProviderRepository;
import com.proveedor.proveedor_mio.service.ProviderService;
import com.proveedor.proveedor_mio.utils.exceptions.BusinessException;
import com.proveedor.proveedor_mio.utils.exceptions.ResourceNotFoundException;
import com.proveedor.proveedor_mio.utils.exceptions.ValidationException;
import com.proveedor.proveedor_mio.utils.mapper.ProviderMapper;
import com.proveedor.proveedor_mio.utils.validation.ValidationUtils;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final String INVALID_PROVIDER_STATUS_MESSAGE = "Invalid provider status value";

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(
        ProviderRepository providerRepository,
        ProviderMapper providerMapper
    ) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    @PostConstruct
    public void initData() {
        providerRepository.initializeData(providerRepository.loadInitialProviders());
    }

    @Override
    public ProviderDTO createProvider(ProviderDTO request) {
        validateProviderPayload(request);

        Provider provider = providerMapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();
        provider.setId(UUID.randomUUID().toString());
        provider.setStatus(ProviderStatus.ACTIVE);
        provider.setCreatedDate(now);
        provider.setModifiedDate(now);

        return providerMapper.toDto(providerRepository.save(provider));
    }

    @Override
    public ProviderDTO getProviderById(String providerId) {
        return providerMapper.toDto(findProviderById(providerId));
    }

    @Override
    public List<ProviderDTO> getProviders(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        List<Provider> providers = parsedStatus == null
            ? providerRepository.findAll()
            : providerRepository.findByStatus(parsedStatus);

        List<ProviderDTO> result = new ArrayList<>();
        for (Provider provider : providers) {
            result.add(providerMapper.toDto(provider));
        }
        return result;
    }

    @Override
    public ProviderDTO updateProvider(String providerId, ProviderDTO request) {
        validateProviderPayload(request);

        Provider existing = findProviderById(providerId);
        validateImmutableEmail(existing.getEmail(), request.getEmail());

        providerMapper.updateDomain(existing, request);
        existing.setModifiedDate(LocalDateTime.now());

        return providerMapper.toDto(providerRepository.save(existing));
    }

    @Override
    public ProviderDTO updateStatus(String providerId, String status) {
        ProviderStatus newStatus = parseRequiredStatus(status);

        Provider existing = findProviderById(providerId);
        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        return providerMapper.toDto(providerRepository.save(existing));
    }

    private Provider findProviderById(String providerId) {
        return providerRepository.findById(providerId)
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));
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

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new BusinessException("Email address cannot be modified once the provider has been created");
        }
    }
}
