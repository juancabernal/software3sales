package com.co.eatupapi.controllers.commercial.sales;

import com.co.eatupapi.dto.commercial.sales.RecipePreparationTraceResponseDTO;
import com.co.eatupapi.services.commercial.sales.RecipePreparationTraceService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commercial/api/v1/sales/preparations")
public class RecipePreparationTraceController {

    private final RecipePreparationTraceService recipePreparationTraceService;

    public RecipePreparationTraceController(RecipePreparationTraceService recipePreparationTraceService) {
        this.recipePreparationTraceService = recipePreparationTraceService;
    }

    @GetMapping
    public ResponseEntity<List<RecipePreparationTraceResponseDTO>> findAll() {
        return ResponseEntity.ok(recipePreparationTraceService.getAllTraces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipePreparationTraceResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(recipePreparationTraceService.getTraceById(id));
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<RecipePreparationTraceResponseDTO>> findBySaleId(@PathVariable UUID saleId) {
        return ResponseEntity.ok(recipePreparationTraceService.getTracesBySaleId(saleId));
    }

    @GetMapping("/sale-detail/{saleDetailId}")
    public ResponseEntity<List<RecipePreparationTraceResponseDTO>> findBySaleDetailId(@PathVariable UUID saleDetailId) {
        return ResponseEntity.ok(recipePreparationTraceService.getTracesBySaleDetailId(saleDetailId));
    }
}
