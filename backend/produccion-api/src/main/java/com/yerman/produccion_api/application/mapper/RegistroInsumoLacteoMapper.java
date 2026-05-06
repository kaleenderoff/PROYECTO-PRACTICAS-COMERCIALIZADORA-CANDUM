package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.RegistroInsumoLacteo;
import com.yerman.produccion_api.infrastructure.entity.RegistroInsumoLacteoEntity;

public class RegistroInsumoLacteoMapper {

    private RegistroInsumoLacteoMapper() {
    }

    public static RegistroInsumoLacteo toDomain(RegistroInsumoLacteoEntity entity) {
        if (entity == null) {
            return null;
        }

        RegistroInsumoLacteo registro = new RegistroInsumoLacteo();
        registro.setId(entity.getId());
        registro.setIdProduccionLactea(entity.getProduccionLactea() != null ? entity.getProduccionLactea().getId() : null);
        registro.setIdProduccionLacteaBatch(entity.getProduccionLacteaBatch() != null ? entity.getProduccionLacteaBatch().getId() : null);
        registro.setIdInsumo(entity.getInsumo() != null ? entity.getInsumo().getId() : null);
        registro.setLoteInsumo(entity.getLoteInsumo());
        registro.setCantidadRequerida(entity.getCantidadRequerida());
        registro.setCantidadUsada(entity.getCantidadUsada());
        registro.setUnidadMedida(entity.getUnidadMedida());
        registro.setFechaHoraRegistro(entity.getFechaHoraRegistro());
        registro.setIdUsuario(entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null);
        registro.setObservaciones(entity.getObservaciones());

        return registro;
    }
}
