package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GestionDashboardUseCase service;

    public DashboardController(GestionDashboardUseCase service) {
        this.service = service;
    }

    @GetMapping("/produccion-vs-empaque")
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        return service.obtenerProduccionVsEmpaque();
    }

    @GetMapping("/trazabilidad-por-lote/{lote}")
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(
            @PathVariable String lote) {
        return service.obtenerTrazabilidadPorLote(lote);
    }
}