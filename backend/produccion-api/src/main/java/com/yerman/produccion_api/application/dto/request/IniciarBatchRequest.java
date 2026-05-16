package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record IniciarBatchRequest(
    @NotNull Long idOrdenProduccion,
    @NotNull Long idMarmita,
    @NotNull BigDecimal kgEntrada
) {}
