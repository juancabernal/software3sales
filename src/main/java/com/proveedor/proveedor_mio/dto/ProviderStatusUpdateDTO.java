package com.proveedor.proveedor_mio.dto;

import com.proveedor.proveedor_mio.domain.ProviderStatus;

public class ProviderStatusUpdateDTO {

    private ProviderStatus status;

    public ProviderStatus getStatus() {
        return status;
    }

    public void setStatus(ProviderStatus status) {
        this.status = status;
    }
}
