package com.proveedor.proveedor_mio.utils.mapper;

import com.proveedor.proveedor_mio.domain.TaxRegime;
import com.proveedor.proveedor_mio.dto.TaxRegimeDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxRegimeMapper {

    public TaxRegimeDTO toDTO(TaxRegime taxRegime) {
        TaxRegimeDTO dto = new TaxRegimeDTO();
        dto.setId(taxRegime.getId());
        dto.setName(taxRegime.getName());
        dto.setStatus(taxRegime.getStatus());
        return dto;
    }

    public TaxRegime toDomain(TaxRegimeDTO dto) {
        TaxRegime taxRegime = new TaxRegime();
        taxRegime.setId(dto.getId());
        taxRegime.setName(dto.getName());
        taxRegime.setStatus(dto.getStatus());
        return taxRegime;
    }
}
