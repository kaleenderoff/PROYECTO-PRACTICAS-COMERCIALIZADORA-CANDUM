package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record ControlCalidadProcesoRequest(
        @NotNull Long idOrdenProduccion,
        Long idEjecucionBatch,
        @NotNull LocalDate fechaProduccion,
        String tipoProducto,
        String producto,
        String lote,
        Integer numeroMarmita,
        String productoEnProceso,
        BigDecimal phLeche,
        BigDecimal acidezLeche,
        BigDecimal densidadLeche,
        BigDecimal grasaLeche,
        LocalTime horaInicioHidrolisis,
        BigDecimal phInicial,
        LocalTime horaFinHidrolisis,
        BigDecimal temperaturaInicial,
        BigDecimal temperaturaFinal,
        BigDecimal acidezInicial,
        BigDecimal acidezFinal,
        BigDecimal phFinal,
        BigDecimal brixInicial,
        BigDecimal brixFinal,
        BigDecimal presion,
        BigDecimal temperaturaCoccion,
        BigDecimal temperaturaEnvasado,
        String colorVisual,
        String saborVisual,
        String texturaVisual,
        String presentacionEnvasado,
        LocalDate fechaVencimiento,
        Boolean liberado,
        Boolean retenido,
        @NotNull Long idRealizadoPor,
        Long idVerificadoPor,
        String observaciones
) {
}
