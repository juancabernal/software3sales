package com.co.eatupapi.controllers.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import com.co.eatupapi.services.commercial.sales.RecipePreparationTraceService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commercial/api/v1/sales")
public class RecipePreparationTraceController {

    private final RecipePreparationTraceService recipePreparationTraceService;

    public RecipePreparationTraceController(RecipePreparationTraceService recipePreparationTraceService) {
        this.recipePreparationTraceService = recipePreparationTraceService;
    }

    @Operation(summary = "Listar trazabilidades por venta")
    @GetMapping("/{saleId}/preparations")
    public ResponseEntity<List<RecipePreparationTraceResponseDTO>> findBySaleId(@PathVariable UUID saleId) {
        return ResponseEntity.ok(recipePreparationTraceService.findBySaleId(saleId));
    }

    @Operation(summary = "Obtener una trazabilidad por id")
    @GetMapping("/preparations/{traceId}")
    public ResponseEntity<RecipePreparationTraceResponseDTO> findById(@PathVariable UUID traceId) {
        return ResponseEntity.ok(recipePreparationTraceService.findById(traceId));
    }
}
