package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EjecucionBatchResponse(
    Long id,
    Long idOrdenProduccion,
    Integer numeroBatch,
    Long idMarmita,
    String nombreMarmita,
    BigDecimal kgEntrada,
    BigDecimal kgProducidos,
    BigDecimal rendimientoPct,
    String estado,
    String observaciones,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    BigDecimal brixFinal
) {}
