package com.proveedor.proveedor_mio.service;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import com.proveedor.proveedor_mio.dto.ProviderDTO;
import com.proveedor.proveedor_mio.repository.ProviderRepository;
import com.proveedor.proveedor_mio.utils.exceptions.BusinessException;
import com.proveedor.proveedor_mio.utils.exceptions.ResourceNotFoundException;
import com.proveedor.proveedor_mio.utils.exceptions.ValidationException;
import com.proveedor.proveedor_mio.utils.mapper.ProviderMapper;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final List<Provider> providers = new ArrayList<>();

    public ProviderService(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    @PostConstruct
    public void initData() {
        providers.clear();
        providers.addAll(providerRepository.loadInitialProviders());
    }

    public ProviderDTO createProvider(ProviderDTO request) {
        validateProviderPayload(request);

        Provider provider = providerMapper.toDomain(request);
        provider.setId(UUID.randomUUID().toString());
        provider.setStatus(ProviderStatus.ACTIVE);
        provider.setCreatedDate(LocalDateTime.now());
        provider.setModifiedDate(LocalDateTime.now());

        providers.add(provider);
        return providerMapper.toDto(provider);
    }

    public ProviderDTO getProviderById(String providerId) {
        Provider provider = findProviderById(providerId);
        return providerMapper.toDto(provider);
    }

    public List<ProviderDTO> getProviders(String status) {
        ProviderStatus parsedStatus = parseStatus(status);

        List<ProviderDTO> result = new ArrayList<>();
        for (Provider provider : providers) {
            if (parsedStatus == null || provider.getStatus() == parsedStatus) {
                result.add(providerMapper.toDto(provider));
            }
        }

        return result;
    }

    public ProviderDTO updateProvider(String providerId, ProviderDTO request) {
        validateProviderPayload(request);

        Provider existing = findProviderById(providerId);
        validateImmutableEmail(existing.getEmail(), request.getEmail());

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

        return providerMapper.toDto(existing);
    }

    public ProviderDTO updateStatus(String providerId, String status) {
        ProviderStatus newStatus = parseRequiredStatus(status);

        Provider existing = findProviderById(providerId);
        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());
        return providerMapper.toDto(existing);
    }

    private Provider findProviderById(String providerId) {
        for (Provider provider : providers) {
            if (provider.getId().equals(providerId)) {
                return provider;
            }
        }

        throw new ResourceNotFoundException("Provider not found with id: " + providerId);
    }

    private ProviderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return ProviderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid provider status value");
        }
    }

    private ProviderStatus parseRequiredStatus(String status) {
        ProviderStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException("Invalid provider status value");
        }
        return parsedStatus;
    }

    private void validateProviderPayload(ProviderDTO request) {
        validateRequiredText(request.getBusinessName(), "businessName");
        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredText(request.getDocumentNumber(), "documentNumber");
        validateRequiredObject(request.getTaxRegimeId(), "taxRegimeId");
        validateRequiredText(request.getResponsibleFirstName(), "responsibleFirstName");
        validateRequiredText(request.getResponsibleLastName(), "responsibleLastName");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredObject(request.getDepartmentId(), "departmentId");
        validateRequiredObject(request.getCityId(), "cityId");
        validateRequiredText(request.getAddress(), "address");
        validateRequiredObject(request.getBranchId(), "branchId");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateDocumentNumber(request.getDocumentNumber());
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

    private void validatePhone(String phone) {
        if (!DIGITS_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Phone number must contain only digits");
        }
        if (phone.length() != 10) {
            throw new ValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        if (!DIGITS_PATTERN.matcher(documentNumber).matches()) {
            throw new ValidationException("Document number must contain only numeric characters");
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new BusinessException("Email address cannot be modified once the provider has been created");
        }
    }
}
