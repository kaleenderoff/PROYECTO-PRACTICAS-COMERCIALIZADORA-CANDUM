package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.LogAuditoriaResponse;
import com.yerman.produccion_api.domain.model.LogAuditoria;

public class LogAuditoriaMapper {

    private LogAuditoriaMapper() {
    }

    public static LogAuditoriaResponse toResponse(LogAuditoria log) {
        LogAuditoriaResponse response = new LogAuditoriaResponse();

        response.setIdLog(log.getIdLog());
        response.setIdUsuario(log.getIdUsuario());
        response.setAccion(log.getAccion().name());
        response.setEntidadAfectada(log.getEntidadAfectada().name());
        response.setIdRegistroAfectado(log.getIdRegistroAfectado());
        response.setDetalle(log.getDetalle());
        response.setFechaHora(log.getFechaHora());

        return response;
    }
}