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
        if (request == null) {
            return null;
        }

        Validacion validacion = new Validacion();
        validacion.setIdDetalleProduccion(request.getIdDetalleProduccion());
        validacion.setIdValidador(request.getIdValidador());
        validacion.setEstado(convertirEstado(request.getEstado()));
        validacion.setObservacion(
                request.getObservacion() != null && !request.getObservacion().trim().isEmpty()
                        ? request.getObservacion().trim()
                        : null);

        return validacion;
    }

    public static ValidacionResponse toResponse(Validacion validacion) {
        if (validacion == null) {
            return null;
        }

        ValidacionResponse response = new ValidacionResponse();
        response.setIdValidacion(validacion.getIdValidacion());
        response.setIdDetalleProduccion(validacion.getIdDetalleProduccion());
        response.setIdValidador(validacion.getIdValidador());
        response.setEstado(validacion.getEstado() != null ? validacion.getEstado().name() : null);
        response.setObservacion(validacion.getObservacion());
        response.setFechaValidacion(validacion.getFechaValidacion());
        response.setCreatedAt(validacion.getCreatedAt());
        response.setUpdatedAt(validacion.getUpdatedAt());

        return response;
    }

    private static EstadoValidacion convertirEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new ReglaNegocioException("El estado de la validación es obligatorio");
        }

        try {
            return EstadoValidacion.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException(
                    "El estado de la validación debe ser VALIDADO o RECHAZADO");
        }
    }
}