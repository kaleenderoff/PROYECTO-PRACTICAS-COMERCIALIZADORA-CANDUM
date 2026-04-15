package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ValidacionRequest;
import com.yerman.produccion_api.application.dto.response.ValidacionResponse;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.Validacion;

public class ValidacionMapper {

    private ValidacionMapper() {
    }

    public static Validacion toDomain(ValidacionRequest request) {
        Validacion v = new Validacion();

        v.setIdDetalleProduccion(request.getIdDetalleProduccion());
        v.setIdValidador(request.getIdValidador());

        try {
            v.setEstado(EstadoValidacion.valueOf(request.getEstado().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Estado de validación inválido");
        }

        v.setObservacion(request.getObservacion());

        return v;
    }

    public static ValidacionResponse toResponse(Validacion v) {
        ValidacionResponse r = new ValidacionResponse();

        r.setIdValidacion(v.getIdValidacion());
        r.setIdDetalleProduccion(v.getIdDetalleProduccion());
        r.setIdValidador(v.getIdValidador());
        r.setEstado(v.getEstado().name());
        r.setObservacion(v.getObservacion());
        r.setFechaValidacion(v.getFechaValidacion());
        r.setCreatedAt(v.getCreatedAt());
        r.setUpdatedAt(v.getUpdatedAt());

        return r;
    }
}