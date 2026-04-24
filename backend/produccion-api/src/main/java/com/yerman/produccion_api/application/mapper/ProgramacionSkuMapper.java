package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.ProgramacionSkuResponse;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;

public class ProgramacionSkuMapper {

    private ProgramacionSkuMapper() {
    }

    public static ProgramacionSku toDomain(ProgramacionSkuEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ProgramacionSku(
                entity.getId(),
                entity.getProgramacion() != null ? entity.getProgramacion().getId() : null,
                entity.getSku() != null ? entity.getSku().getId() : null,
                entity.getUnidadesObjetivo(),
                entity.getObservaciones());
    }

    public static ProgramacionSkuEntity toEntity(
            ProgramacionSku domain,
            ProgramacionProduccionEntity programacion,
            CatalogoSkuEntity sku) {
        if (domain == null) {
            return null;
        }

        ProgramacionSkuEntity entity = new ProgramacionSkuEntity();
        entity.setProgramacion(programacion);
        entity.setSku(sku);
        entity.setUnidadesObjetivo(domain.getUnidadesObjetivo());
        entity.setObservaciones(domain.getObservaciones());
        return entity;
    }

    public static ProgramacionSkuResponse toResponse(ProgramacionSku domain) {
        if (domain == null) {
            return null;
        }

        return new ProgramacionSkuResponse(
                domain.getId(),
                domain.getIdProgramacion(),
                domain.getIdSku(),
                domain.getUnidadesObjetivo(),
                domain.getObservaciones());
    }
}