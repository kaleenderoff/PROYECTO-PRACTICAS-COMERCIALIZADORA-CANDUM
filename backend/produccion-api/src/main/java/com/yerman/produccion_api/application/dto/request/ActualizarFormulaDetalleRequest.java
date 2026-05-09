package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ActualizarFormulaDetalleRequest(

        @Positive(message = "La cantidad en kg debe ser mayor a cero") BigDecimal cantidadKg,

        BigDecimal porcentaje,

        Boolean esCritico,

        Integer ordenAdicion) {
}