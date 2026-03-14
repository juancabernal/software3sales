package com.proveedor.proveedor_mio.service;

import com.proveedor.proveedor_mio.dto.TaxRegimeDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaxRegimeService {

    private static final List<TaxRegimeDTO> TAX_REGIMES = List.of(
        new TaxRegimeDTO("1", "Comun"),
        new TaxRegimeDTO("2", "Simplificado"),
        new TaxRegimeDTO("3", "Gran Contribuyente")
    );

    public List<TaxRegimeDTO> getTaxRegimes() {
        return TAX_REGIMES;
    }
}
