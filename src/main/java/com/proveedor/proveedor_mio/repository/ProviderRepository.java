package com.proveedor.proveedor_mio.repository;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import java.util.List;
import java.util.Optional;

public interface ProviderRepository {

    void initializeData(List<Provider> providers);

    Provider save(Provider provider);

    Optional<Provider> findById(String providerId);

    List<Provider> findAll();

    List<Provider> findByStatus(ProviderStatus status);

    List<Provider> loadInitialProviders();
}
