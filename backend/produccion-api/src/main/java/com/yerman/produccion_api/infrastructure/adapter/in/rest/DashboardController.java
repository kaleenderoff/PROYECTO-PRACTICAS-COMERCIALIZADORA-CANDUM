package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionPendienteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionResponse;
import com.yerman.produccion_api.application.service.DashboardExcelService;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final GestionDashboardUseCase gestionDashboardUseCase;
    private final DashboardExcelService dashboardExcelService;

    public DashboardController(
            GestionDashboardUseCase gestionDashboardUseCase,
            DashboardExcelService dashboardExcelService) {
        this.gestionDashboardUseCase = gestionDashboardUseCase;
        this.dashboardExcelService = dashboardExcelService;
    }

    @GetMapping("/resumen-general")
    public ResponseEntity<DashboardResumenResponse> obtenerResumenGeneral() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerResumenGeneral());
    }

    @GetMapping("/produccion-por-sku")
    public ResponseEntity<List<DashboardProduccionPorSkuResponse>> obtenerProduccionPorSku() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerProduccionPorSku());
    }

    @GetMapping("/produccion-vs-empaque")
    public ResponseEntity<List<DashboardProduccionVsEmpaqueResponse>> obtenerProduccionVsEmpaque() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerProduccionVsEmpaque());
    }

    @GetMapping("/trazabilidad/lote/{lote}")
    public ResponseEntity<DashboardTrazabilidadLoteResponse> obtenerTrazabilidadPorLote(
            @PathVariable String lote) {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerTrazabilidadPorLote(lote));
    }

    @GetMapping("/validaciones")
    public ResponseEntity<List<DashboardValidacionResponse>> obtenerValidaciones() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerValidaciones());
    }

    @GetMapping("/validaciones-pendientes")
    public ResponseEntity<List<DashboardValidacionPendienteResponse>> obtenerValidacionesPendientes() {
        return ResponseEntity.ok(gestionDashboardUseCase.obtenerValidacionesPendientes());
    }

    @GetMapping("/exportar/validaciones")
    public ResponseEntity<byte[]> exportarValidaciones() throws IOException {
        byte[] excel = dashboardExcelService.exportarValidaciones();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("dashboard-validaciones.xlsx").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }

    @GetMapping("/exportar/validaciones-pendientes")
    public ResponseEntity<byte[]> exportarValidacionesPendientes() throws IOException {
        byte[] excel = dashboardExcelService.exportarValidacionesPendientes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("dashboard-validaciones-pendientes.xlsx").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }

    @GetMapping("/exportar/produccion-por-sku")
    public ResponseEntity<byte[]> exportarProduccionPorSku() throws IOException {
        byte[] excel = dashboardExcelService.exportarProduccionPorSku();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("dashboard-produccion-por-sku.xlsx").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }

    @GetMapping("/exportar/produccion-vs-empaque")
    public ResponseEntity<byte[]> exportarProduccionVsEmpaque() throws IOException {
        byte[] excel = dashboardExcelService.exportarProduccionVsEmpaque();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("dashboard-produccion-vs-empaque.xlsx").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }

    @GetMapping("/exportar/trazabilidad/lote/{lote}")
    public ResponseEntity<byte[]> exportarTrazabilidadPorLote(@PathVariable String lote) throws IOException {
        byte[] excel = dashboardExcelService.exportarTrazabilidadPorLote(lote);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("dashboard-trazabilidad-" + lote + ".xlsx").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(excel);
    }
}