package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.DescremadoRecepcion;
import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;

public class DescremadoRecepcionMapper {

    private DescremadoRecepcionMapper() {
    }

    public static DescremadoRecepcion toDomain(DescremadoRecepcionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new DescremadoRecepcion(
                entity.getId(),
                entity.getRecepcionLeche() != null ? entity.getRecepcionLeche().getId() : null,
                entity.getLitrosDescremados(),
                entity.getCremaObtenidaKg(),
                entity.getObservaciones());
    }
}