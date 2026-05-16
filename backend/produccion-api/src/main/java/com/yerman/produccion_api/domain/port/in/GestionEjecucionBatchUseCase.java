package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.EjecucionBatch;
import java.math.BigDecimal;
import java.util.List;

public interface GestionEjecucionBatchUseCase {
    EjecucionBatch iniciarBatch(Long idOrden, Long idMarmita, BigDecimal kgEntrada);
    EjecucionBatch finalizarBatch(Long idBatch, BigDecimal kgProducidos, String observaciones, Boolean conNovedad, Boolean huboReproceso, Boolean batchConforme);
    EjecucionBatch registrarNovedad(Long idBatch, String observaciones);
    List<EjecucionBatch> listarBatchesPorOrden(Long idOrden);
    EjecucionBatch obtenerBatch(Long idBatch);
    void eliminarBatch(Long idBatch);
}
