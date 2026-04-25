package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.OrdenProduccionResponse;
import com.yerman.produccion_api.domain.model.OrdenProduccion;

public class OrdenProduccionRestMapper {

    private OrdenProduccionRestMapper() {
    }

    public static OrdenProduccionResponse toResponse(OrdenProduccion orden) {
        if (orden == null) {
            return null;
        }

        return new OrdenProduccionResponse(
                orden.getId(),
                orden.getNumeroOrden(),
                orden.getIdProgramacion(),
                orden.getIdLinea(),
                orden.getIdProducto(),
                orden.getIdTurno(),
                orden.getIdJefeLineaEjecutor(),
                orden.getIdCreadaPor(),
                orden.getFechaProduccion(),
                orden.getEstado(),
                orden.getObservaciones(),
                orden.getFechaInicioReal(),
                orden.getFechaFinReal());
    }
}