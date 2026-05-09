package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ActualizarFormulaVersionRequest(

        LocalDate fechaInicioVigencia,

        LocalDate fechaFinVigencia,

        @Positive(message = "Los kg por batch deben ser mayores a cero") BigDecimal kgBatchTotal,

        BigDecimal reduccionEvaporacionPct,

        BigDecimal rendimientoTeoricoPct,

        BigDecimal brixObjetivoMin,

        BigDecimal brixObjetivoMax,

        @Size(max = 100, message = "El campo aprobado por no puede superar 100 caracteres") String aprobadoPor,

        @Size(max = 500, message = "El documento de aprobación no puede superar 500 caracteres") String documentoAprobacion,

        String observacionesTecnicas) {
}