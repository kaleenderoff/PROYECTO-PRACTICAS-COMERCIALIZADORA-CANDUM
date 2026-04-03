package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ValidacionRequest;
import com.yerman.produccion_api.application.dto.response.ValidacionResponse;
import com.yerman.produccion_api.domain.model.Validacion;

public class ValidacionMapper {

    private ValidacionMapper() {
    }

    public static Validacion toDomain(ValidacionRequest request) {
        Validacion validacion = new Validacion();
        validacion.setIdDetalleProduccion(request.getIdDetalleProduccion());
        validacion.setIdValidador(request.getIdValidador());
        validacion.setEstado(request.getEstado());
        validacion.setObservacion(request.getObservacion());
        return validacion;
    }

    public static ValidacionResponse toResponse(Validacion validacion) {
        ValidacionResponse response = new ValidacionResponse();
        response.setIdValidacion(validacion.getIdValidacion());
        response.setIdDetalleProduccion(validacion.getIdDetalleProduccion());
        response.setIdValidador(validacion.getIdValidador());
        response.setEstado(validacion.getEstado());
        response.setObservacion(validacion.getObservacion());
        response.setFechaValidacion(validacion.getFechaValidacion());
        response.setCreatedAt(validacion.getCreatedAt());
        response.setUpdatedAt(validacion.getUpdatedAt());
        return response;
    }
}
