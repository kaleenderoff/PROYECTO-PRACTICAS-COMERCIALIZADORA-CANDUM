package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReporteConsumoInsumosLacteoResponse(
        LocalDate fecha,
        TotalesConsumoInsumosResponse totales,
        List<DetalleConsumoInsumoResponse> detalles) {

    public record TotalesConsumoInsumosResponse(
            Integer registros,
            Integer producciones,
            Integer batches,
            BigDecimal cantidadRequeridaTotal,
            BigDecimal cantidadUsadaTotal,
            BigDecimal diferenciaTotal) {
    }

    public record DetalleConsumoInsumoResponse(
            Long idRegistro,
            LocalDate fechaProduccion,
            Long idProduccion,
            String producto,
            Long idBatch,
            Integer numeroBatch,
            Long idInsumo,
            String codigoInsumo,
            String insumo,
            String tipoInsumo,
            String loteInsumo,
            BigDecimal cantidadRequerida,
            BigDecimal cantidadUsada,
            BigDecimal diferencia,
            String unidadMedida,
            LocalDateTime fechaHoraRegistro,
            Long idUsuario,
            String usuario,
            String observaciones) {
    }
}
