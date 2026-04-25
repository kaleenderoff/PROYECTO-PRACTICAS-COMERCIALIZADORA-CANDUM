package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionDetalleEntity;

public class OrdenProduccionDetalleMapper {

    private OrdenProduccionDetalleMapper() {
    }

    public static OrdenProduccionDetalle toDomain(OrdenProduccionDetalleEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrdenProduccionDetalle(
                entity.getId(),
                entity.getOrden().getId(),
                entity.getProgramacionSku() != null ? entity.getProgramacionSku().getId() : null,
                entity.getSku().getId(),
                entity.getCantidadProgramada(),
                entity.getUnidadProgramada(),
                entity.getPrioridad(),
                entity.getObservaciones());
    }
}