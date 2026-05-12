package com.yerman.produccion_api.application.dto.request;

import com.yerman.produccion_api.domain.model.TipoCalculoInsumo;

import java.math.BigDecimal;

public record ActualizarFormulaDetalleRequest(
                TipoCalculoInsumo tipoCalculo,

                BigDecimal cantidadKg,

                BigDecimal porcentaje,

                Boolean esCritico,

                Integer ordenAdicion) {
}