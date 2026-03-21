package com.proveedor.proveedor_mio.repository;

import com.proveedor.proveedor_mio.domain.TaxRegime;
import com.proveedor.proveedor_mio.domain.TaxRegimeStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRegimeRepository extends JpaRepository<TaxRegime, UUID> {

    List<TaxRegime> findByStatus(TaxRegimeStatus status);

    Optional<TaxRegime> findByName(String name);
}
