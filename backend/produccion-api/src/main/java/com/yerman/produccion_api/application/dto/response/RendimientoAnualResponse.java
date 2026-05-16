package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public record RendimientoAnualResponse(
        Integer mes,
        BigDecimal ptDulceLeche,
        BigDecimal ptLecheCondensada,
        BigDecimal lecheRecibida,
        BigDecimal rendimientoDL,
        BigDecimal rendimientoLC
) {}
