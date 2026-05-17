package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ControlPesoProductoRequest(
        @NotNull Long idOrdenProduccion,
        Long idEjecucionBatch,
        Long idSku,
        @NotNull LocalDate fechaControl,
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
        @NotNull Long idRealizadoPor,
        Long idVerificadoPor,
        String observaciones,
        @Valid List<ControlPesoMuestraRequest> muestras
) {
}
