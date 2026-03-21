package com.proveedor.proveedor_mio.repository;

import com.proveedor.proveedor_mio.domain.TaxRegime;
import com.proveedor.proveedor_mio.domain.TaxRegimeStatus;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TaxRegimeRepositoryImpl implements TaxRegimeRepository {

    private final Map<String, TaxRegime> taxRegimes = new LinkedHashMap<>();

    @Override
    public void initializeData(List<TaxRegime> initialTaxRegimes) {
        taxRegimes.clear();
        for (TaxRegime taxRegime : initialTaxRegimes) {
            taxRegimes.put(taxRegime.getId(), taxRegime);
        }
    }

    @Override
    public TaxRegime save(TaxRegime taxRegime) {
        taxRegimes.put(taxRegime.getId(), taxRegime);
        return taxRegime;
    }

    @Override
    public Optional<TaxRegime> findById(String id) {
        return Optional.ofNullable(taxRegimes.get(id));
    }

    @Override
    public List<TaxRegime> findAll() {
        return new ArrayList<>(taxRegimes.values());
    }

    @Override
    public List<TaxRegime> findByStatus(TaxRegimeStatus status) {
        List<TaxRegime> result = new ArrayList<>();
        for (TaxRegime taxRegime : taxRegimes.values()) {
            if (taxRegime.getStatus() == status) {
                result.add(taxRegime);
            }
        }
        return result;
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        for (TaxRegime taxRegime : taxRegimes.values()) {
            if (taxRegime.getName() != null && taxRegime.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByNameIgnoreCaseAndIdNot(String name, String id) {
        for (TaxRegime taxRegime : taxRegimes.values()) {
            if (taxRegime.getName() != null
                && taxRegime.getName().equalsIgnoreCase(name)
                && !taxRegime.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
