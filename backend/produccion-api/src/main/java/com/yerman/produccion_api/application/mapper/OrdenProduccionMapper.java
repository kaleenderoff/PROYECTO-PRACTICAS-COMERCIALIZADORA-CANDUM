package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.OrdenProduccionResponse;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;

public class OrdenProduccionMapper {

    private OrdenProduccionMapper() {
    }

    public static OrdenProduccion toDomain(OrdenProduccionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrdenProduccion(
                entity.getId(),
                entity.getNumeroOrden(),
                entity.getProgramacion().getId(),
                entity.getLinea().getId(),
                entity.getProducto().getId(),
                entity.getTurno().getId(),
                entity.getJefeLineaEjecutor() != null ? entity.getJefeLineaEjecutor().getIdUsuario() : null,
                entity.getCreadaPor().getIdUsuario(),
                entity.getFechaProduccion(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getFechaInicioReal(),
                entity.getFechaFinReal());
    }

    public static OrdenProduccionResponse toResponse(OrdenProduccionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrdenProduccionResponse(
                entity.getId(),
                entity.getNumeroOrden(),
                entity.getProgramacion().getId(),
                entity.getLinea().getId(),
                entity.getLinea().getNombre(),
                entity.getProducto().getId(),
                entity.getProducto().getNombre(),
                entity.getTurno().getId(),
                entity.getTurno().getNombre(),
                entity.getJefeLineaEjecutor() != null ? entity.getJefeLineaEjecutor().getIdUsuario() : null,
                nombreCompleto(entity.getJefeLineaEjecutor()),
                entity.getCreadaPor().getIdUsuario(),
                nombreCompleto(entity.getCreadaPor()),
                entity.getFechaProduccion(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getFechaInicioReal(),
                entity.getFechaFinReal());
    }

    private static String nombreCompleto(UsuarioEntity usuario) {
        if (usuario == null) {
            return null;
        }

        return String.join(" ",
                usuario.getPrimerNombre() != null ? usuario.getPrimerNombre() : "",
                usuario.getSegundoNombre() != null ? usuario.getSegundoNombre() : "",
                usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : "",
                usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : "")
                .trim();
    }
}