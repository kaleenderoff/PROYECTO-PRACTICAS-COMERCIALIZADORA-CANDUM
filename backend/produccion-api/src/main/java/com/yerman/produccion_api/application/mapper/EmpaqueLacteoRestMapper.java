package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.EmpaqueLacteoRequest;
import com.yerman.produccion_api.application.dto.response.EmpaqueLacteoResponse;
import com.yerman.produccion_api.domain.model.EmpaqueLacteo;

public class EmpaqueLacteoRestMapper {

    private EmpaqueLacteoRestMapper() {
    }

    public static EmpaqueLacteo toDomain(EmpaqueLacteoRequest request) {
        if (request == null) {
            return null;
        }

        EmpaqueLacteo empaqueLacteo = new EmpaqueLacteo();

        empaqueLacteo.setProductoTerminadoLacteoId(request.getProductoTerminadoLacteoId());
        empaqueLacteo.setProduccionLacteaBatchId(request.getProduccionLacteaBatchId());
        empaqueLacteo.setLoteEmpaque(request.getLoteEmpaque());
        empaqueLacteo.setFechaEmpaque(request.getFechaEmpaque());
        empaqueLacteo.setFechaVencimiento(request.getFechaVencimiento());
        empaqueLacteo.setKilosUtilizados(request.getKilosUtilizados());
        empaqueLacteo.setUnidades(request.getUnidades());
        empaqueLacteo.setCajas(request.getCajas());
        empaqueLacteo.setPesoTotalKg(request.getPesoTotalKg());
        empaqueLacteo.setObservaciones(request.getObservaciones());

        return empaqueLacteo;
    }

    public static EmpaqueLacteoResponse toResponse(EmpaqueLacteo domain) {
        if (domain == null) {
            return null;
        }

        EmpaqueLacteoResponse response = new EmpaqueLacteoResponse();

        response.setId(domain.getId());
        response.setProductoTerminadoLacteoId(domain.getProductoTerminadoLacteoId());
        response.setProduccionLacteaBatchId(domain.getProduccionLacteaBatchId());
        response.setLoteEmpaque(domain.getLoteEmpaque());
        response.setFechaEmpaque(domain.getFechaEmpaque());
        response.setFechaVencimiento(domain.getFechaVencimiento());
        response.setKilosUtilizados(domain.getKilosUtilizados());
        response.setUnidades(domain.getUnidades());
        response.setCajas(domain.getCajas());
        response.setPesoTotalKg(domain.getPesoTotalKg());

        if (domain.getEstado() != null) {
            response.setEstado(domain.getEstado().name());
        }

        response.setObservaciones(domain.getObservaciones());

        return response;
    }
}