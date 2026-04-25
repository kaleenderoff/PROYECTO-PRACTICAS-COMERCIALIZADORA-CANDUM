package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.OrdenProduccionDetalleResponse;
import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;

public class OrdenProduccionDetalleRestMapper {

    private OrdenProduccionDetalleRestMapper() {
    }

    public static OrdenProduccionDetalleResponse toResponse(OrdenProduccionDetalle detalle) {
        return new OrdenProduccionDetalleResponse(
                detalle.getId(),
                detalle.getIdOrden(),
                detalle.getIdProgramacionSku(),
                detalle.getIdSku(),
                detalle.getCantidadProgramada(),
                detalle.getUnidadProgramada(),
                detalle.getPrioridad(),
                detalle.getObservaciones());
    }
}