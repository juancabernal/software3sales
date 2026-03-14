package com.proveedor.proveedor_mio.repository;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderRepository {

    public List<Provider> loadInitialProviders() {
        LocalDateTime now = LocalDateTime.now();

        Provider provider1 = new Provider();
        provider1.setId("provider-001");
        provider1.setBusinessName("Distribuidora Alimentos SAS");
        provider1.setDocumentTypeId(1L);
        provider1.setDocumentNumber("900123456");
        provider1.setTaxRegimeId(10L);
        provider1.setResponsibleFirstName("Carlos");
        provider1.setResponsibleLastName("Ramirez");
        provider1.setPhone("3004567890");
        provider1.setEmail("contacto@empresa.com");
        provider1.setDepartmentId(11L);
        provider1.setCityId(11001L);
        provider1.setAddress("Calle 10 #45-23");
        provider1.setBranchId(1L);
        provider1.setStatus(ProviderStatus.ACTIVE);
        provider1.setCreatedDate(now.minusDays(5));
        provider1.setModifiedDate(now.minusDays(1));

        Provider provider2 = new Provider();
        provider2.setId("provider-002");
        provider2.setBusinessName("Logistica y Suministros LTDA");
        provider2.setDocumentTypeId(1L);
        provider2.setDocumentNumber("901987654");
        provider2.setTaxRegimeId(10L);
        provider2.setResponsibleFirstName("Laura");
        provider2.setResponsibleLastName("Gomez");
        provider2.setPhone("3206541230");
        provider2.setEmail("ventas@logistica.com");
        provider2.setDepartmentId(5L);
        provider2.setCityId(5001L);
        provider2.setAddress("Carrera 50 #12-99");
        provider2.setBranchId(2L);
        provider2.setStatus(ProviderStatus.ACTIVE);
        provider2.setCreatedDate(now.minusDays(10));
        provider2.setModifiedDate(now.minusDays(2));

        Provider provider3 = new Provider();
        provider3.setId("provider-003");
        provider3.setBusinessName("Tecnologia Integral S.A.");
        provider3.setDocumentTypeId(1L);
        provider3.setDocumentNumber("900765432");
        provider3.setTaxRegimeId(20L);
        provider3.setResponsibleFirstName("Javier");
        provider3.setResponsibleLastName("Mendoza");
        provider3.setPhone("3101239876");
        provider3.setEmail("info@tecintegral.co");
        provider3.setDepartmentId(76L);
        provider3.setCityId(76001L);
        provider3.setAddress("Avenida 6N #34-10");
        provider3.setBranchId(3L);
        provider3.setStatus(ProviderStatus.INACTIVE);
        provider3.setCreatedDate(now.minusDays(15));
        provider3.setModifiedDate(now.minusDays(3));

        return List.of(provider1, provider2, provider3);
    }
}
