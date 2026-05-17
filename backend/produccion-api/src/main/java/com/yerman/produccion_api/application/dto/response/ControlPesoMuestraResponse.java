package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public record ControlPesoMuestraResponse(
        Long id,
        Integer numeroMuestra,
        BigDecimal pesoBruto,
        BigDecimal tara,
        BigDecimal pesoNeto
) {
}
