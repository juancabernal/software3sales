package com.proveedor.proveedor_mio.repository;

import com.proveedor.proveedor_mio.domain.TaxRegime;
import com.proveedor.proveedor_mio.domain.TaxRegimeStatus;
import java.util.List;
import java.util.Optional;

public interface TaxRegimeRepository {

    void initializeData(List<TaxRegime> taxRegimes);

    TaxRegime save(TaxRegime taxRegime);

    Optional<TaxRegime> findById(String id);

    List<TaxRegime> findAll();

    List<TaxRegime> findByStatus(TaxRegimeStatus status);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, String id);
}
