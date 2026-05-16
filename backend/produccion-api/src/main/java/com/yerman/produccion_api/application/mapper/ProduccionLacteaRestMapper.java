package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ProduccionLacteaBatchRequest;
import com.yerman.produccion_api.application.dto.request.ProduccionLacteaRequest;
import com.yerman.produccion_api.application.dto.response.ProduccionLacteaBatchResponse;
import com.yerman.produccion_api.application.dto.response.ProduccionLacteaResponse;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionBatch;

import java.util.List;
import java.util.Collections;

public class ProduccionLacteaRestMapper {

    private ProduccionLacteaRestMapper() {
    }

    public static Produccion toDomain(ProduccionLacteaRequest request) {
        if (request == null)
            return null;

        List<ProduccionBatch> batches = request.getBatches() != null 
                ? request.getBatches().stream()
                    .map(ProduccionLacteaRestMapper::toDomainBatch)
                    .toList()
                : Collections.emptyList();

        Produccion produccion = new Produccion();
        produccion.setIdOrdenProduccion(request.getIdOrdenProduccion());
        produccion.setFechaProduccion(request.getFechaProduccion());
        produccion.setProducto(request.getProducto());
        produccion.setIdTanque(request.getIdTanque());
        produccion.setIdUsuario(request.getIdUsuario());
        produccion.setObservaciones(request.getObservaciones());
        produccion.setBatches(batches);

        return produccion;
    }

    private static ProduccionBatch toDomainBatch(ProduccionLacteaBatchRequest request) {
        if (request == null) return null;
        ProduccionBatch batch = new ProduccionBatch();
        batch.setNumeroBatch(request.getNumeroBatch());
        batch.setIdMarmita(request.getIdMarmita());
        batch.setLitrosConsumidos(request.getLitrosConsumidos());
        batch.setKilosProducidos(request.getKilosProducidos());
        batch.setObservaciones(request.getObservaciones());
        return batch;
    }

    public static ProduccionLacteaResponse toResponse(Produccion produccion) {
        if (produccion == null) {
            return null;
        }

        List<ProduccionLacteaBatchResponse> batches = produccion.getBatches() == null 
                ? Collections.emptyList()
                : produccion.getBatches().stream()
                        .map(ProduccionLacteaRestMapper::toResponseBatch)
                        .toList();

        return new ProduccionLacteaResponse(
                produccion.getId(),
                produccion.getIdOrdenProduccion(),
                produccion.getFechaProduccion(),
                produccion.getProducto(),
                produccion.getIdTanque(),
                produccion.getIdUsuario(),
                produccion.getObservaciones(),
                batches);
    }

    private static ProduccionLacteaBatchResponse toResponseBatch(ProduccionBatch batch) {
        if (batch == null) return null;
        return new ProduccionLacteaBatchResponse(
                batch.getId(),
                batch.getNumeroBatch(),
                batch.getIdMarmita(),
                batch.getLitrosConsumidos(),
                batch.getKilosProducidos(),
                batch.getRendimiento(),
                batch.getIdMovimientoLeche());
    }
}
