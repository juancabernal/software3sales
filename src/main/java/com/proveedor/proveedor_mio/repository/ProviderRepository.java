package com.proveedor.proveedor_mio.repository;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {

    List<Provider> findByStatus(ProviderStatus status);

    Optional<Provider> findByEmail(String email);
}
