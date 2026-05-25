package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ValidacionOrdenProduccionRequest(
        @NotNull Long idOrden,
        @NotNull Boolean aprobado,
        @NotNull Long idJefeProduccion,
        @Size(max = 500) String observacion,
        Boolean requiereRevision) {
}
