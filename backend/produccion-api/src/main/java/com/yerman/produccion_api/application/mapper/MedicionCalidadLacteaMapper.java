package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;
import com.yerman.produccion_api.infrastructure.entity.MedicionCalidadLacteaEntity;

public class MedicionCalidadLacteaMapper {

    private MedicionCalidadLacteaMapper() {
    }

    public static MedicionCalidadLactea toDomain(MedicionCalidadLacteaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MedicionCalidadLactea(
                entity.getId(),
                entity.getProduccionLactea() != null ? entity.getProduccionLactea().getId() : null,
                entity.getProduccionLacteaBatch() != null ? entity.getProduccionLacteaBatch().getId() : null,
                entity.getTipoMedicion(),
                entity.getReferencia(),
                entity.getBrix(),
                entity.getPh(),
                entity.getFechaHoraMedicion(),
                entity.getUsuarioCalidad() != null ? entity.getUsuarioCalidad().getIdUsuario() : null,
                entity.getObservaciones());
    }
}
