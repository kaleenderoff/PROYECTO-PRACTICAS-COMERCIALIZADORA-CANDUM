package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.EjecucionBatch;
import com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch;

import java.util.List;
import java.util.Optional;

public interface EjecucionBatchRepositoryPort {
    EjecucionBatch guardar(EjecucionBatch batch);
    Optional<EjecucionBatch> obtenerPorId(Long id);
    List<EjecucionBatch> listarPorOrden(Long idOrden);
    Optional<EjecucionBatch> obtenerPorOrdenYNumero(Long idOrden, Integer numeroBatch);
    boolean existeMarmitaOcupadaEnOrden(Long idMarmita, Long idOrden);
    void eliminar(Long id);
}
