package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionBatch;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaEntity;

import java.util.List;

public class ProduccionLacteaMapper {

    private ProduccionLacteaMapper() {
    }

    public static Produccion toDomain(ProduccionLacteaEntity entity) {
        if (entity == null)
            return null;

        Produccion produccion = new Produccion();
        produccion.setId(entity.getId());
        produccion.setIdOrdenProduccion(entity.getOrdenProduccion() != null ? entity.getOrdenProduccion().getId() : null);
        produccion.setFechaProduccion(entity.getFechaProduccion());
        produccion.setProducto(entity.getProducto());
        produccion.setIdTanque(entity.getTanque() != null ? entity.getTanque().getId() : null);
        produccion.setIdUsuario(entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null);
        produccion.setObservaciones(entity.getObservaciones());

        if (entity.getBatches() != null) {
            produccion.setBatches(entity.getBatches().stream()
                    .map(ProduccionLacteaMapper::toDomainBatch)
                    .toList());
        } else {
            produccion.setBatches(java.util.List.of());
        }

        return produccion;
    }

    private static ProduccionBatch toDomainBatch(ProduccionLacteaBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        ProduccionBatch batch = new ProduccionBatch();
        batch.setId(entity.getId());
        batch.setIdMarmita(entity.getMarmita() != null ? entity.getMarmita().getId() : null);
        batch.setNumeroBatch(entity.getNumeroBatch());
        batch.setLitrosConsumidos(entity.getLitrosConsumidos());
        batch.setKilosProducidos(entity.getKilosProducidos());
        batch.setRendimiento(entity.getRendimiento());
        batch.setIdMovimientoLeche(entity.getMovimientoLeche() != null ? entity.getMovimientoLeche().getId() : null);
        batch.setObservaciones(entity.getObservaciones());
        return batch;
    }
}
