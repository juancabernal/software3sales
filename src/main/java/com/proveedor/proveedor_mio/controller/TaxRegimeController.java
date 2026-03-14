package com.proveedor.proveedor_mio.controller;

import com.proveedor.proveedor_mio.dto.TaxRegimeDTO;
import com.proveedor.proveedor_mio.service.TaxRegimeService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commercial/api/v1/tax-regimes")
public class TaxRegimeController {

    private final TaxRegimeService taxRegimeService;

    public TaxRegimeController(TaxRegimeService taxRegimeService) {
        this.taxRegimeService = taxRegimeService;
    }

    @GetMapping
    public ResponseEntity<List<TaxRegimeDTO>> getTaxRegimes() {
        List<TaxRegimeDTO> taxRegimes = taxRegimeService.getTaxRegimes();
        return ResponseEntity.ok(taxRegimes);
    }
}
