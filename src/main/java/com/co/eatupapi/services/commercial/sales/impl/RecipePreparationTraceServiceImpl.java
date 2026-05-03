package com.co.eatupapi.services.commercial.sales.impl;

import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceDomain;
import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceStatus;
import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import com.co.eatupapi.repositories.commercial.sales.RecipePreparationTraceRepository;
import com.co.eatupapi.services.commercial.sales.RecipePreparationTraceService;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleNotFoundException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleValidationException;
import com.co.eatupapi.utils.commercial.sales.mapper.RecipePreparationTraceMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipePreparationTraceServiceImpl implements RecipePreparationTraceService {

    private final RecipePreparationTraceRepository traceRepository;
    private final RecipePreparationTraceMapper traceMapper;

    public RecipePreparationTraceServiceImpl(RecipePreparationTraceRepository traceRepository,
                                             RecipePreparationTraceMapper traceMapper) {
        this.traceRepository = traceRepository;
        this.traceMapper = traceMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreparationTraceResponseDTO> getAllTraces() {
        return traceMapper.toDtoList(traceRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreparationTraceResponseDTO> getTracesBySaleId(UUID saleId) {
        if (saleId == null) {
            throw new SaleValidationException("El saleId es obligatorio.");
        }
        return traceMapper.toDtoList(traceRepository.findBySaleId(saleId));
    }

    @Override
    @Transactional(readOnly = true)
    public RecipePreparationTraceResponseDTO getTraceById(UUID id) {
        if (id == null) {
            throw new SaleValidationException("El id de la trazabilidad es obligatorio.");
        }

        RecipePreparationTraceDomain trace = traceRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("No se encontró la trazabilidad con id: " + id));

        return traceMapper.toDto(trace);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreparationTraceResponseDTO> getTracesBySaleDetailId(UUID saleDetailId) {
        if (saleDetailId == null) {
            throw new SaleValidationException("El saleDetailId es obligatorio.");
        }
        return traceMapper.toDtoList(traceRepository.findBySaleDetailId(saleDetailId));
    }

    @Override
    @Transactional
    public void deleteTracesBySaleId(UUID saleId) {
        if (saleId == null) {
            throw new SaleValidationException("El saleId es obligatorio.");
        }
        traceRepository.deleteBySaleId(saleId);
    }

    @Override
    @Transactional
    public void createInitialTraces(SaleDomain sale) {
        if (sale == null) {
            throw new SaleValidationException("La venta es obligatoria para crear trazabilidades.");
        }
        if (sale.getId() == null) {
            throw new SaleValidationException("El id de la venta es obligatorio para crear trazabilidades.");
        }
        if (sale.getDetails() == null || sale.getDetails().isEmpty()) {
            throw new SaleValidationException("La venta debe tener detalles para crear trazabilidades.");
        }

        List<RecipePreparationTraceDomain> traces = sale.getDetails().stream()
                .map(detail -> buildInitialTrace(sale, detail))
                .toList();

        traceRepository.saveAll(traces);
    }

    private RecipePreparationTraceDomain buildInitialTrace(SaleDomain sale, SaleDetailDomain detail) {
        RecipePreparationTraceDomain trace = new RecipePreparationTraceDomain();
        trace.setSaleId(sale.getId());
        trace.setSaleDetailId(detail.getId());
        trace.setRecipeId(detail.getRecipeId());
        // TODO: Reemplazar este estado temporal con la respuesta real de inventory (ACCEPTED/REJECTED).
        trace.setStatus(RecipePreparationTraceStatus.ACCEPTED);
        trace.setObservation(null);
        return trace;
    }
}
