package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.OrdenProduccionResponse;
import com.yerman.produccion_api.domain.model.OrdenProduccion;

public class OrdenProduccionRestMapper {

    private OrdenProduccionRestMapper() {
    }

    public static OrdenProduccionResponse toResponse(OrdenProduccion orden) {

        if (orden == null) {
            return null;
        }

        var skus = orden.getSkus() != null
                ? orden.getSkus().stream().map(ProgramacionSkuMapper::toResponse).toList()
                : java.util.List.<com.yerman.produccion_api.application.dto.response.ProgramacionSkuResponse>of();

        java.math.BigDecimal kgPTTotalPlan = skus.stream()
                .map(s -> s.getKgProductoTerminado() != null ? s.getKgProductoTerminado() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal kgEntradaTotalPlan = skus.stream()
                .map(s -> s.getKgBatchCalculado() != null ? s.getKgBatchCalculado() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        var response = new OrdenProduccionResponse(
                orden.getId(),
                orden.getNumeroOrden(),
                orden.getIdProgramacion(),
                orden.getIdLinea(),
                orden.getNombreLinea(),
                orden.getIdProducto(),
                orden.getNombreProducto(),
                orden.getIdTurno(),
                orden.getNombreTurno(),
                orden.getIdJefeLineaEjecutor(),
                orden.getNombreJefeLineaEjecutor(),
                orden.getIdCreadaPor(),
                orden.getNombreCreadaPor(),
                orden.getFechaProduccion(),
                orden.getEstado(),
                orden.getObservaciones(),
                orden.getFechaInicioReal(),
                orden.getFechaFinReal(),
                orden.getNumBachesPlan(),
                orden.getKgBachePlan(),
                kgPTTotalPlan,
                kgEntradaTotalPlan,
                orden.getNombreFormula(),
                orden.getVersionFormula(),
                skus);

        response.setKgEntradaReal(orden.getKgEntradaReal());
        response.setKgProducidoBatches(orden.getKgProducidoBatches());
        response.setKgPtReal(orden.getKgPtReal());
        response.setRendimientoReal(orden.getRendimientoReal());
        response.setMermaReal(orden.getMermaReal());
        response.setMermaEmpaque(orden.getMermaEmpaque());
        response.setIdTanqueLeche(orden.getIdTanqueLeche());
        response.setNombreTanqueLeche(orden.getNombreTanqueLeche());

        return response;
    }
}