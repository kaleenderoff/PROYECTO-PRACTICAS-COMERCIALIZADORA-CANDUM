package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CalidadRecepcionLecheResponse(
        Long id,
        Long idRecepcionLeche,
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
        Long idRealizadoPor,
        String observaciones,
        LocalDateTime createdAt) {
}
