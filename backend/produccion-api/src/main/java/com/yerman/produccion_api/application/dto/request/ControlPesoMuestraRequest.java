package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ControlPesoMuestraRequest(
        @NotNull @Min(1) Integer numeroMuestra,
        BigDecimal pesoBruto,
        BigDecimal tara,
        @NotNull BigDecimal pesoNeto
) {
}
