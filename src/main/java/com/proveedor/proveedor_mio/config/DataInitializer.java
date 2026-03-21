package com.proveedor.proveedor_mio.config;

import com.proveedor.proveedor_mio.domain.Provider;
import com.proveedor.proveedor_mio.domain.ProviderStatus;
import com.proveedor.proveedor_mio.domain.TaxRegime;
import com.proveedor.proveedor_mio.domain.TaxRegimeStatus;
import com.proveedor.proveedor_mio.repository.ProviderRepository;
import com.proveedor.proveedor_mio.repository.TaxRegimeRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TaxRegimeRepository taxRegimeRepository;
    private final ProviderRepository providerRepository;

    public DataInitializer(TaxRegimeRepository taxRegimeRepository, ProviderRepository providerRepository) {
        this.taxRegimeRepository = taxRegimeRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public void run(String... args) {
        initializeTaxRegimes();
        initializeProviders();
    }

    private void initializeTaxRegimes() {
        if (taxRegimeRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        taxRegimeRepository.save(createTaxRegime("Común", now.minusSeconds(3)));
        taxRegimeRepository.save(createTaxRegime("Simplificado", now.minusSeconds(2)));
        taxRegimeRepository.save(createTaxRegime("Gran Contribuyente", now.minusSeconds(1)));
    }

    private TaxRegime createTaxRegime(String name, LocalDateTime timestamp) {
        TaxRegime taxRegime = new TaxRegime();
        taxRegime.setName(name);
        taxRegime.setStatus(TaxRegimeStatus.ACTIVE);
        taxRegime.setCreatedAt(timestamp);
        taxRegime.setModifiedAt(timestamp);
        return taxRegime;
    }

    private void initializeProviders() {
        if (providerRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        providerRepository.save(createProvider(
            "Distribuidora Alimentos SAS",
            1L,
            "900123456",
            10L,
            "Carlos",
            "Ramirez",
            "3004567890",
            "contacto@empresa.com",
            11L,
            11001L,
            "Calle 10 #45-23",
            1L,
            ProviderStatus.ACTIVE,
            now.minusDays(15),
            now.minusDays(11)
        ));
        providerRepository.save(createProvider(
            "Logistica y Suministros LTDA",
            1L,
            "901987654",
            10L,
            "Laura",
            "Gomez",
            "3206541230",
            "ventas@logistica.com",
            5L,
            5001L,
            "Carrera 50 #12-99",
            2L,
            ProviderStatus.ACTIVE,
            now.minusDays(10),
            now.minusDays(6)
        ));
        providerRepository.save(createProvider(
            "Tecnologia Integral S.A.",
            1L,
            "900765432",
            20L,
            "Javier",
            "Mendoza",
            "3101239876",
            "info@tecintegral.co",
            76L,
            76001L,
            "Avenida 6N #34-10",
            3L,
            ProviderStatus.INACTIVE,
            now.minusDays(5),
            now.minusDays(3)
        ));
    }

    private Provider createProvider(
        String businessName,
        Long documentTypeId,
        String documentNumber,
        Long taxRegimeId,
        String responsibleFirstName,
        String responsibleLastName,
        String phone,
        String email,
        Long departmentId,
        Long cityId,
        String address,
        Long branchId,
        ProviderStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
    ) {
        Provider provider = new Provider();
        provider.setBusinessName(businessName);
        provider.setDocumentTypeId(documentTypeId);
        provider.setDocumentNumber(documentNumber);
        provider.setTaxRegimeId(taxRegimeId);
        provider.setResponsibleFirstName(responsibleFirstName);
        provider.setResponsibleLastName(responsibleLastName);
        provider.setPhone(phone);
        provider.setEmail(email);
        provider.setDepartmentId(departmentId);
        provider.setCityId(cityId);
        provider.setAddress(address);
        provider.setBranchId(branchId);
        provider.setStatus(status);
        provider.setCreatedAt(createdAt);
        provider.setModifiedAt(modifiedAt);
        return provider;
    }
}
