package com.co.eatupapi.services.commercial.sales.impl;

import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import com.co.eatupapi.repositories.commercial.sales.RecipePreparationTraceRepository;
import com.co.eatupapi.services.commercial.sales.RecipePreparationTraceService;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleNotFoundException;
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
    public List<RecipePreparationTraceResponseDTO> findBySaleId(UUID saleId) {
        return traceRepository.findBySaleId(saleId).stream().map(traceMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecipePreparationTraceResponseDTO findById(UUID traceId) {
        return traceRepository.findById(traceId)
                .map(traceMapper::toDto)
                .orElseThrow(() -> new SaleNotFoundException("No existe trazabilidad con el id: " + traceId));
    }
}
