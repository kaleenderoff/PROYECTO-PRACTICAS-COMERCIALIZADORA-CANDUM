package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDateTime;

public record ValidacionOrdenProduccionResponse(
        Long id,
        Long idOrden,
        String numeroOrden,
        Boolean aprobado,
        Long idJefeProduccion,
        String observacion,
        LocalDateTime fechaValidacion,
        Boolean requiereRevision) {
}
