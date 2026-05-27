package com.yerman.produccion_api.application.dto.response;

public record EstadoCalidadRecepcionResponse(
        Long idRecepcionLeche,
        String estadoCalidad
) {}
