package com.yerman.produccion_api.application.dto.request;

import com.yerman.produccion_api.domain.model.TipoCalculoInsumo;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CrearFormulaDetalleRequest(
                @NotNull(message = "El insumo es obligatorio") Long idInsumo,

                TipoCalculoInsumo tipoCalculo,

                BigDecimal cantidadKg,

                BigDecimal porcentaje,

                Boolean esCritico,

                Integer ordenAdicion) {
}