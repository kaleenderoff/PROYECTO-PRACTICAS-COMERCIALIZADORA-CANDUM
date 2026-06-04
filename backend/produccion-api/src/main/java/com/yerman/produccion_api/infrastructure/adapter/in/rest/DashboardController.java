package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.*;
import com.yerman.produccion_api.application.service.DashboardExcelService;
import com.yerman.produccion_api.application.service.GestionOrdenProduccionService;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GestionDashboardUseCase service;
    private final DashboardExcelService dashboardExcelService;
    private final GestionOrdenProduccionService ordenService;

    public DashboardController(GestionDashboardUseCase service,
            DashboardExcelService dashboardExcelService,
            GestionOrdenProduccionService ordenService) {
        this.service = service;
        this.dashboardExcelService = dashboardExcelService;
        this.ordenService = ordenService;
    }

    @PostMapping("/resync-reporte")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION','JEFE_PLANTA')")
    public ResponseEntity<Map<String, String>> resyncReporte(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        ordenService.resincronizarReporteDiario(fecha);
        return ResponseEntity.ok(Map.of("mensaje", "Reporte resincronizado para " + fecha));
    }

    @GetMapping("/produccion-vs-empaque")
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        return service.obtenerProduccionVsEmpaque();
    }

    @GetMapping("/gerencial")
    public DashboardGerencialResponse obtenerDashboardGerencial(
            @RequestParam int mes,
            @RequestParam int anio) {
        return service.obtenerDashboardGerencial(mes, anio);
    }

    @GetMapping("/produccion-sku")
    public List<DashboardProduccionSkuResponse> obtenerProduccionSkuMensual(
            @RequestParam int mes,
            @RequestParam int anio) {
        return service.obtenerProduccionSkuMensual(mes, anio);
    }

    @GetMapping("/rendimiento-anual")
    public List<RendimientoAnualResponse> obtenerRendimientoAnual(
            @RequestParam int anio) {
        return service.obtenerRendimientoAnual(anio);
    }

    @GetMapping("/trazabilidad-por-lote/{lote}")
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(
            @PathVariable String lote) {
        return service.obtenerTrazabilidadPorLote(lote);
    }

    @GetMapping("/produccion-vs-empaque/excel")
    public ResponseEntity<byte[]> descargarExcelProduccionVsEmpaque() {
        byte[] excel = dashboardExcelService.generarExcelProduccionVsEmpaque();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=produccion_vs_empaque.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excel);
    }
}