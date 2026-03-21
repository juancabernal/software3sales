package com.proveedor.proveedor_mio.service;

import com.proveedor.proveedor_mio.dto.TaxRegimeDTO;
import java.util.List;

public interface TaxRegimeService {

    TaxRegimeDTO createTaxRegime(TaxRegimeDTO request);

    TaxRegimeDTO getTaxRegimeById(String taxRegimeId);

    List<TaxRegimeDTO> getTaxRegimes(String status);

    TaxRegimeDTO updateTaxRegime(String taxRegimeId, TaxRegimeDTO request);

    TaxRegimeDTO changeStatus(String taxRegimeId, String status);
}
