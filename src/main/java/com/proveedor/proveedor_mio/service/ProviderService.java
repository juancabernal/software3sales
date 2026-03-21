package com.proveedor.proveedor_mio.service;

import com.proveedor.proveedor_mio.dto.ProviderDTO;
import java.util.List;

public interface ProviderService {

    ProviderDTO createProvider(ProviderDTO request);

    ProviderDTO getProviderById(String providerId);

    List<ProviderDTO> getProviders(String status);

    ProviderDTO updateProvider(String providerId, ProviderDTO request);

    ProviderDTO updateStatus(String providerId, String status);
}
