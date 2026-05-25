package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.EjecucionBatchResponse;
import com.yerman.produccion_api.domain.model.EjecucionBatch;

public class EjecucionBatchRestMapper {
    
    private EjecucionBatchRestMapper() {}

    public static EjecucionBatchResponse toResponse(EjecucionBatch domain) {
        if (domain == null) return null;
        
        return new EjecucionBatchResponse(
                domain.getId(),
                domain.getIdOrdenProduccion(),
                domain.getNumeroBatch(),
                domain.getIdMarmita(),
                domain.getNombreMarmita(),
                domain.getKgEntrada(),
                domain.getKgProducidos(),
                domain.getRendimientoPct(),
                domain.getEstado().name(),
                domain.getObservaciones(),
                domain.getFechaInicio(),
                domain.getFechaFin(),
                domain.getBrixFinal()
        );
    }
}
