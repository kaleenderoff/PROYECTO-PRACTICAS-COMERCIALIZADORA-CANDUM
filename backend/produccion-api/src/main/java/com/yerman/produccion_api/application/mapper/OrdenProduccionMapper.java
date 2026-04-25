package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;

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
}