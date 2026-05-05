package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GestionDashboardUseCase gestionDashboardUseCase;

    public DashboardController(GestionDashboardUseCase gestionDashboardUseCase) {
        this.gestionDashboardUseCase = gestionDashboardUseCase;
    }

    @GetMapping("/produccion-vs-empaque")
    public ResponseEntity<List<DashboardProduccionVsEmpaqueResponse>> obtenerProduccionVsEmpaque() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerProduccionVsEmpaque());
    }
}