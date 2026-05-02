package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import java.util.List;
import java.util.UUID;

public interface RecipePreparationTraceService {
    List<RecipePreparationTraceResponseDTO> findBySaleId(UUID saleId);
    RecipePreparationTraceResponseDTO findById(UUID traceId);
}
