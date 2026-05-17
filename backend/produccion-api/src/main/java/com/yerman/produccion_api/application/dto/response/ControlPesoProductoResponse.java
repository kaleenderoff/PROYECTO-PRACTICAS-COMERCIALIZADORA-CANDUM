package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ControlPesoProductoResponse(
        Long id,
        Long idOrdenProduccion,
        Long idEjecucionBatch,
        Long idSku,
        LocalDate fechaControl,
        String producto,
        String marca,
        String lote,
        LocalDate fechaVencimiento,
        String presentacion,
        String numeroTanda,
        String rangoBatches,
        BigDecimal pesoBrutoPromedio,
        BigDecimal taraPromedio,
        BigDecimal pesoNetoPromedio,
        Boolean aparienciaOk,
        Boolean etiquetadoOk,
        Boolean tapadoOk,
        Integer cantidadPorCaja,
        Boolean liberado,
        Boolean retenido,
        Long idRealizadoPor,
        Long idVerificadoPor,
        String observaciones,
        LocalDateTime createdAt,
        List<ControlPesoMuestraResponse> muestras
) {
}
