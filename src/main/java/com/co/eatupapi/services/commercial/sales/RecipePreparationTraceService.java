package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import java.util.List;
import java.util.UUID;

public interface RecipePreparationTraceService {
    List<RecipePreparationTraceResponseDTO> getTracesBySaleId(UUID saleId);
    RecipePreparationTraceResponseDTO getTraceById(UUID id);
    List<RecipePreparationTraceResponseDTO> getTracesBySaleDetailId(UUID saleDetailId);
    void deleteTracesBySaleId(UUID saleId);
    void createInitialTraces(SaleDomain sale);
}
