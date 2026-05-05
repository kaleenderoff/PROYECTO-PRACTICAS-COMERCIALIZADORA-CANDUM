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

        List<ProduccionBatch> batches = entity.getBatches() == null ? List.of()
                : entity.getBatches().stream()
                        .map(ProduccionLacteaMapper::toDomainBatch)
                        .toList();

        Produccion produccion = new Produccion();
        produccion.setId(entity.getId());
        produccion.setIdOrdenProduccion(entity.getOrdenProduccion() != null ? entity.getOrdenProduccion().getId() : null);
        produccion.setFechaProduccion(entity.getFechaProduccion());
        produccion.setProducto(entity.getProducto());
        produccion.setIdTanque(entity.getTanque().getId());
        produccion.setIdUsuario(entity.getUsuario().getIdUsuario());
        produccion.setObservaciones(entity.getObservaciones());
        produccion.setBatches(batches);

        return produccion;
    }

    private static ProduccionBatch toDomainBatch(ProduccionLacteaBatchEntity entity) {
        ProduccionBatch batch = new ProduccionBatch();
        batch.setId(entity.getId());
        batch.setNumeroBatch(entity.getNumeroBatch());
        batch.setIdMarmita(entity.getMarmita().getId());
        batch.setLitrosConsumidos(entity.getLitrosConsumidos());
        batch.setKilosProducidos(entity.getKilosProducidos());
        batch.setRendimiento(entity.getRendimiento());

        if (entity.getMovimientoLeche() != null) {
            batch.setIdMovimientoLeche(entity.getMovimientoLeche().getId());
        }

        return batch;
    }
}
