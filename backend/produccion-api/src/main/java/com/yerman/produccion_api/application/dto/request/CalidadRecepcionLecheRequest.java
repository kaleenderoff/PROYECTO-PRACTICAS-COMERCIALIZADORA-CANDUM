package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CalidadRecepcionLecheRequest(
        @NotNull Long idRecepcionLeche,
        LocalDateTime fechaControl,
        Boolean pruebaAlcoholOk,
        Boolean lactoscanOk,
        BigDecimal acidez,
        BigDecimal densidad,
        BigDecimal grasa,
        BigDecimal aguaPct,
        BigDecimal temperatura,
        BigDecimal ph,
        Boolean aprobado,
        Boolean retenido,
        @NotNull Long idRealizadoPor,
        String observaciones) {
}
