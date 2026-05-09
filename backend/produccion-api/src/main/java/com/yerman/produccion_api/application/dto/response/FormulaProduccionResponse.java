package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FormulaProduccionResponse(
        Long idFormulaVersion,
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
        List<FormulaDetalleResponse> detalles) {

    public record FormulaDetalleResponse(
            Long idDetalle,
            Long idInsumo,
            String codigoInsumo,
            String nombreInsumo,
            String unidadMedida,
            BigDecimal cantidadKg,
            BigDecimal porcentaje,
            Boolean esCritico,
            Integer ordenAdicion) {
    }
}