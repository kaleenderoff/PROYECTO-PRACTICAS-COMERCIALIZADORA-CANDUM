package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;

public class MovimientoLecheMapper {

    private MovimientoLecheMapper() {
    }

    public static MovimientoLeche toDomain(MovimientoLecheEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MovimientoLeche(
                entity.getId(),
                entity.getTanque().getId(),
                entity.getTipoMovimiento(),
                entity.getFechaHora(),
                entity.getCantidadLitros(),
                entity.getSaldoResultanteLitros(),
                entity.getUsuario().getIdUsuario(),
                entity.getReferencia(),
                entity.getObservaciones());
    }
}