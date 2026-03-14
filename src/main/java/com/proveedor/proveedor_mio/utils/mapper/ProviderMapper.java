package com.proveedor.proveedor_mio.utils.mapper;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.dto.ProviderDTO;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDTO toDto(Provider provider) {
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

    public Provider toDomain(ProviderDTO dto) {
        Provider provider = new Provider();
        provider.setId(dto.getId());
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
        provider.setStatus(dto.getStatus());
        provider.setCreatedDate(dto.getCreatedDate());
        provider.setModifiedDate(dto.getModifiedDate());
        return provider;
    }
}
