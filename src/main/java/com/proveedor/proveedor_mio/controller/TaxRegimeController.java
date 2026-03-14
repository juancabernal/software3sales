package com.proveedor.proveedor_mio.controller;

import com.proveedor.proveedor_mio.dto.TaxRegimeDTO;
import com.proveedor.proveedor_mio.service.TaxRegimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/commercial/api/v1/tax-regimes")
public class TaxRegimeController {

    private final TaxRegimeService taxRegimeService;

    public TaxRegimeController(TaxRegimeService taxRegimeService) {
        this.taxRegimeService = taxRegimeService;
    }

    @GetMapping
    public Flux<TaxRegimeDTO> getTaxRegimes() {
        return taxRegimeService.getTaxRegimes();
    }
}
