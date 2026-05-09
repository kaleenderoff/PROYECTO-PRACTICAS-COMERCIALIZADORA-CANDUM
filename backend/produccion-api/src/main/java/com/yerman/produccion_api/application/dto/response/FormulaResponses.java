package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class FormulaResponses {

    private FormulaResponses() {
    }

    public record FormulaResponse(
            Long id,
            String nombre,
            Long idProducto,
            String nombreProducto,
            Boolean activo,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    public record FormulaVersionResponse(
            Long id,
            Long idFormula,
            String nombreFormula,
            Long idProducto,
            String nombreProducto,
            String version,
            LocalDate fechaInicioVigencia,
            LocalDate fechaFinVigencia,
            BigDecimal kgBatchTotal,
            BigDecimal reduccionEvaporacionPct,
            BigDecimal rendimientoTeoricoPct,
            BigDecimal brixObjetivoMin,
            BigDecimal brixObjetivoMax,
            String estado,
            String aprobadoPor,
            String documentoAprobacion,
            String observacionesTecnicas,
            Long idCreadoPor,
            String nombreCreadoPor,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<FormulaDetalleResponse> detalles) {
    }

    public record FormulaDetalleResponse(
            Long id,
            Long idFormulaVersion,
            Long idInsumo,
            String codigoInsumo,
            String nombreInsumo,
            String unidadMedida,
            BigDecimal cantidadKg,
            BigDecimal porcentaje,
            Boolean esCritico,
            Integer ordenAdicion,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }
}