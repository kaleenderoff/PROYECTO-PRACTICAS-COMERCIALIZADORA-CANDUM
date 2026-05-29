package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record FinalizarBatchRequest(
        @NotNull @DecimalMin(value = "0.001", message = "Los kg producidos deben ser mayores a cero.") BigDecimal kgProducidos,

        String observaciones,

        Boolean conNovedad,
        Boolean huboReproceso,
        Boolean batchConforme,

        @NotNull(message = "El Brix final es obligatorio para finalizar el batch.") @DecimalMin(value = "0.001", message = "El Brix final debe ser mayor a cero.") BigDecimal brixFinal,

        String tipoNovedad) {
}