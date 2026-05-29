package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.EjecucionBatchResponse;
import com.yerman.produccion_api.domain.model.EjecucionBatch;

public class EjecucionBatchRestMapper {

    private EjecucionBatchRestMapper() {
    }

    public static EjecucionBatchResponse toResponse(EjecucionBatch domain) {
        if (domain == null)
            return null;

        return new EjecucionBatchResponse(
                domain.getId(),
                domain.getIdOrdenProduccion(),
                domain.getNumeroBatch(),
                domain.getIdMarmita(),
                domain.getNombreMarmita(),
                domain.getIdMovimientoLeche(),
                domain.getLitrosLecheDescontados(),
                domain.getSaldoResultanteLecheLitros(),
                domain.getKgEntrada(),
                domain.getKgProducidos(),
                domain.getRendimientoPct(),
                domain.getEstado().name(),
                domain.getObservaciones(),
                domain.getHuboReproceso(),
                domain.getBatchConforme(),
                domain.getFechaInicio(),
                domain.getFechaFin(),
                domain.getBrixFinal(),
                domain.getTipoNovedad());
    }
}