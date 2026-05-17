package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ControlCalidadProcesoResponse(
        Long id,
        Long idOrdenProduccion,
        Long idEjecucionBatch,
        LocalDate fechaProduccion,
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
        Long idRealizadoPor,
        Long idVerificadoPor,
        String observaciones,
        LocalDateTime createdAt
) {
}
