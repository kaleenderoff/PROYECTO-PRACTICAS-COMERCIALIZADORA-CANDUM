package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse;
import com.yerman.produccion_api.application.service.ReporteLacteoExcelService;
import com.yerman.produccion_api.application.service.ReporteLacteoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes/lacteos")
public class ReporteLacteoController {

    private final ReporteLacteoService reporteLacteoService;
    private final ReporteLacteoExcelService reporteLacteoExcelService;

    public ReporteLacteoController(
            ReporteLacteoService reporteLacteoService,
            ReporteLacteoExcelService reporteLacteoExcelService) {
        this.reporteLacteoService = reporteLacteoService;
        this.reporteLacteoExcelService = reporteLacteoExcelService;
    }

    @GetMapping("/consumo-insumos")
    public ReporteConsumoInsumosLacteoResponse consultarConsumoInsumos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return reporteLacteoService.consultarConsumoInsumos(fecha);
    }

    @GetMapping("/consumo-insumos/excel")
    public ResponseEntity<byte[]> descargarExcelConsumoInsumos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaConsulta = fecha != null ? fecha : LocalDate.now();
        byte[] excel = reporteLacteoExcelService.generarExcelConsumoInsumos(fechaConsulta);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=consumo_insumos_lacteos_" + fechaConsulta + ".xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excel);
    }

    @GetMapping("/recepcion-descremado")
    public ReporteRecepcionDescremadoLacteoResponse consultarRecepcionDescremado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return reporteLacteoService.consultarRecepcionDescremado(fecha);
    }

    @GetMapping("/recepcion-descremado/excel")
    public ResponseEntity<byte[]> descargarExcelRecepcionDescremado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaConsulta = fecha != null ? fecha : LocalDate.now();
        byte[] excel = reporteLacteoExcelService.generarExcelRecepcionDescremado(fechaConsulta);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=recepcion_descremado_lacteos_" + fechaConsulta + ".xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excel);
    }
}
