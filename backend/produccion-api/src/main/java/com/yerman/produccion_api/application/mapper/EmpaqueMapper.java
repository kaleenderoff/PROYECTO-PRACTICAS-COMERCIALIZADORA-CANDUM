package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.EmpaqueRequest;
import com.yerman.produccion_api.application.dto.response.EmpaqueResponse;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Empaque;
import com.yerman.produccion_api.domain.model.ProductoTerminado;

public class EmpaqueMapper {

    private EmpaqueMapper() {
    }

    public static Empaque toDomain(EmpaqueRequest request) {
        if (request == null) {
            return null;
        }

        Empaque domain = new Empaque();

        if (request.getIdDetalleProduccion() != null) {
            DetalleProduccion detalleProduccion = new DetalleProduccion();
            detalleProduccion.setIdDetalleProduccion(request.getIdDetalleProduccion());
            domain.setDetalleProduccion(detalleProduccion);
        }

        if (request.getIdProductoTerminado() != null) {
            ProductoTerminado productoTerminado = new ProductoTerminado();
            productoTerminado.setId(request.getIdProductoTerminado());
            domain.setProductoTerminado(productoTerminado);
        }

        domain.setLoteEmpaque(request.getLoteEmpaque());
        domain.setFechaEmpaque(request.getFechaEmpaque());
        domain.setFechaVencimiento(request.getFechaVencimiento());
        domain.setEstado(request.getEstado());
        domain.setCantidadUnidades(request.getCantidadUnidades());
        domain.setCantidadCajas(request.getCantidadCajas());
        domain.setPesoTotalKg(request.getPesoTotalKg());
        domain.setObservaciones(request.getObservaciones());

        return domain;
    }

    public static EmpaqueResponse toResponse(Empaque domain) {
        if (domain == null) {
            return null;
        }

        EmpaqueResponse response = new EmpaqueResponse();
        response.setId(domain.getId());
        response.setLoteEmpaque(domain.getLoteEmpaque());
        response.setFechaEmpaque(domain.getFechaEmpaque());
        response.setFechaVencimiento(domain.getFechaVencimiento());
        response.setEstado(domain.getEstado());
        response.setCantidadUnidades(domain.getCantidadUnidades());
        response.setCantidadCajas(domain.getCantidadCajas());
        response.setPesoTotalKg(domain.getPesoTotalKg());
        response.setObservaciones(domain.getObservaciones());
        response.setCreatedAt(domain.getCreatedAt());
        response.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getDetalleProduccion() != null) {
            response.setIdDetalleProduccion(domain.getDetalleProduccion().getIdDetalleProduccion());
        }

        if (domain.getProductoTerminado() != null) {
            response.setIdProductoTerminado(domain.getProductoTerminado().getId());
            response.setSkuProductoTerminado(domain.getProductoTerminado().getSku());
            response.setNombreComercialProductoTerminado(domain.getProductoTerminado().getNombreComercial());
        }

        return response;
    }
}