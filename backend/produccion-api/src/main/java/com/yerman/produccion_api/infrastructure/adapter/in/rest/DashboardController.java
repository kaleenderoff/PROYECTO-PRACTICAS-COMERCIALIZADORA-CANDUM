package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GestionDashboardUseCase gestionDashboardUseCase;

    public DashboardController(GestionDashboardUseCase gestionDashboardUseCase) {
        this.gestionDashboardUseCase = gestionDashboardUseCase;
    }

    @GetMapping("/resumen-general")
    public ResponseEntity<DashboardResumenResponse> obtenerResumenGeneral() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerResumenGeneral());
    }

    @GetMapping("/produccion-por-sku")
    public ResponseEntity<List<DashboardProduccionPorSkuResponse>> obtenerProduccionPorSku() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerProduccionPorSku());
    }
}