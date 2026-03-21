package com.proveedor.proveedor_mio.service.impl;

import com.proveedor.proveedor_mio.domain.TaxRegime;
import com.proveedor.proveedor_mio.domain.TaxRegimeStatus;
import com.proveedor.proveedor_mio.dto.TaxRegimeDTO;
import com.proveedor.proveedor_mio.repository.TaxRegimeRepository;
import com.proveedor.proveedor_mio.service.TaxRegimeService;
import com.proveedor.proveedor_mio.utils.exceptions.BusinessException;
import com.proveedor.proveedor_mio.utils.exceptions.ResourceNotFoundException;
import com.proveedor.proveedor_mio.utils.exceptions.ValidationException;
import com.proveedor.proveedor_mio.utils.mapper.TaxRegimeMapper;
import com.proveedor.proveedor_mio.utils.validation.ValidationUtils;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TaxRegimeServiceImpl implements TaxRegimeService {

    private static final String INVALID_TAX_REGIME_STATUS_MESSAGE = "Tax regime status must be ACTIVE or INACTIVE";

    private final TaxRegimeRepository taxRegimeRepository;
    private final TaxRegimeMapper taxRegimeMapper;

    public TaxRegimeServiceImpl(TaxRegimeRepository taxRegimeRepository, TaxRegimeMapper taxRegimeMapper) {
        this.taxRegimeRepository = taxRegimeRepository;
        this.taxRegimeMapper = taxRegimeMapper;
    }

    @Override
    public TaxRegimeDTO createTaxRegime(TaxRegimeDTO request) {
        validateRequestBody(request);
        validateName(request.getName());
        validateIdNotEditable(request.getId());
        validateNameDoesNotExist(request.getName());

        TaxRegime taxRegime = taxRegimeMapper.toDomain(request);
        LocalDateTime now = LocalDateTime.now();
        taxRegime.setStatus(TaxRegimeStatus.ACTIVE);
        taxRegime.setCreatedAt(now);
        taxRegime.setModifiedAt(now);

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(taxRegime));
    }

    @Override
    public TaxRegimeDTO getTaxRegimeById(String taxRegimeId) {
        return taxRegimeMapper.toDTO(findTaxRegimeById(taxRegimeId));
    }

    @Override
    public List<TaxRegimeDTO> getTaxRegimes(String status) {
        TaxRegimeStatus parsedStatus = parseStatus(status);
        List<TaxRegime> taxRegimes = parsedStatus == null
            ? taxRegimeRepository.findAll()
            : taxRegimeRepository.findByStatus(parsedStatus);

        return taxRegimes.stream()
            .sorted(Comparator.comparing(TaxRegime::getCreatedAt))
            .map(taxRegimeMapper::toDTO)
            .toList();
    }

    @Override
    public TaxRegimeDTO updateTaxRegime(String taxRegimeId, TaxRegimeDTO request) {
        validateRequestBody(request);
        validateName(request.getName());
        validateStatus(request.getStatus());
        validateIdNotEditable(request.getId());

        TaxRegime existing = findTaxRegimeById(taxRegimeId);
        validateNameDoesNotExistForDifferentId(request.getName(), existing.getId());

        taxRegimeMapper.updateDomain(existing, request);
        existing.setModifiedAt(LocalDateTime.now());

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(existing));
    }

    @Override
    public TaxRegimeDTO changeStatus(String taxRegimeId, String status) {
        TaxRegimeStatus newStatus = parseRequiredStatus(status);

        TaxRegime existing = findTaxRegimeById(taxRegimeId);
        existing.setStatus(newStatus);
        existing.setModifiedAt(LocalDateTime.now());

        return taxRegimeMapper.toDTO(taxRegimeRepository.save(existing));
    }

    private TaxRegime findTaxRegimeById(String taxRegimeId) {
        UUID parsedId = parseUuid(taxRegimeId, "Tax regime not found with id: " + taxRegimeId);
        return taxRegimeRepository.findById(parsedId)
            .orElseThrow(() -> new ResourceNotFoundException("Tax regime not found with id: " + taxRegimeId));
    }

    private UUID parseUuid(String value, String notFoundMessage) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(notFoundMessage);
        }
    }

    private TaxRegimeStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return TaxRegimeStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(INVALID_TAX_REGIME_STATUS_MESSAGE);
        }
    }

    private TaxRegimeStatus parseRequiredStatus(String status) {
        TaxRegimeStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException(INVALID_TAX_REGIME_STATUS_MESSAGE);
        }
        return parsedStatus;
    }

    private void validateRequestBody(TaxRegimeDTO request) {
        ValidationUtils.requireObject(request, "Tax regime request cannot be null");
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Tax regime name cannot be null or empty");
        }
    }

    private void validateStatus(TaxRegimeStatus status) {
        if (status == null) {
            throw new ValidationException(INVALID_TAX_REGIME_STATUS_MESSAGE);
        }
    }

    private void validateIdNotEditable(String id) {
        if (id != null && !id.isBlank()) {
            throw new BusinessException("Tax regime id cannot be modified");
        }
    }

    private void validateNameDoesNotExist(String name) {
        String normalizedName = name.trim();
        boolean exists = taxRegimeRepository.findAll().stream()
            .map(TaxRegime::getName)
            .filter(existingName -> existingName != null)
            .anyMatch(existingName -> existingName.equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new BusinessException("Tax regime already exists with name: " + normalizedName);
        }
    }

    private void validateNameDoesNotExistForDifferentId(String name, UUID id) {
        String normalizedName = name.trim();
        boolean exists = taxRegimeRepository.findAll().stream()
            .filter(taxRegime -> !taxRegime.getId().equals(id))
            .map(TaxRegime::getName)
            .filter(existingName -> existingName != null)
            .anyMatch(existingName -> existingName.equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new BusinessException("Tax regime already exists with name: " + normalizedName);
        }
    }
}
