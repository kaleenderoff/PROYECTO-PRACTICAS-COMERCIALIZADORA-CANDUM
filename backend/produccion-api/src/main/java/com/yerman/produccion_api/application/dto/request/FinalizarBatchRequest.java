package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record FinalizarBatchRequest(
    @NotNull BigDecimal kgProducidos,
    String observaciones,
    Boolean conNovedad,
    Boolean huboReproceso,
    Boolean batchConforme
) {}
