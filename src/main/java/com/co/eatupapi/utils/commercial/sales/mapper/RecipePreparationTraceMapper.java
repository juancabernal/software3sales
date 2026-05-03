package com.co.eatupapi.utils.commercial.sales.mapper;

import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceDomain;
import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RecipePreparationTraceMapper {

    public RecipePreparationTraceResponseDTO toDto(RecipePreparationTraceDomain domain) {
        if (domain == null) {
            return null;
        }

        RecipePreparationTraceResponseDTO dto = new RecipePreparationTraceResponseDTO();
        dto.setId(domain.getId());
        dto.setSaleId(domain.getSaleId());
        dto.setSaleDetailId(domain.getSaleDetailId());
        dto.setRecipeId(domain.getRecipeId());
        dto.setStatus(domain.getStatus());
        dto.setObservation(domain.getObservation());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setModifiedDate(domain.getModifiedDate());
        return dto;
    }

    public List<RecipePreparationTraceResponseDTO> toDtoList(List<RecipePreparationTraceDomain> domains) {
        if (domains == null || domains.isEmpty()) {
            return Collections.emptyList();
        }

        return domains.stream().map(this::toDto).toList();
    }
}
