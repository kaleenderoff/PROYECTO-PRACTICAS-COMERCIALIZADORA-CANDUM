package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ProductoTerminadoRequest;
import com.yerman.produccion_api.application.dto.response.ProductoTerminadoResponse;
import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.model.ProductoTerminado;

public class ProductoTerminadoMapper {

    private ProductoTerminadoMapper() {
    }

    public static ProductoTerminado toDomain(ProductoTerminadoRequest request) {
        if (request == null) {
            return null;
        }

        Producto productoBase = new Producto();
        productoBase.setIdProducto(request.getIdProductoBase());

        ProductoTerminado domain = new ProductoTerminado();
        domain.setProductoBase(productoBase);
        domain.setSku(request.getSku());
        domain.setNombreComercial(request.getNombreComercial());
        domain.setReferencia(request.getReferencia());
        domain.setGramajeG(request.getGramajeG());
        domain.setUnidadMedida(request.getUnidadMedida());
        domain.setEmbalaje(request.getEmbalaje());
        domain.setActivo(request.getActivo());

        return domain;
    }

    public static ProductoTerminadoResponse toResponse(ProductoTerminado domain) {
        if (domain == null) {
            return null;
        }

        ProductoTerminadoResponse response = new ProductoTerminadoResponse();
        response.setId(domain.getId());
        response.setSku(domain.getSku());
        response.setNombreComercial(domain.getNombreComercial());
        response.setReferencia(domain.getReferencia());
        response.setGramajeG(domain.getGramajeG());
        response.setUnidadMedida(domain.getUnidadMedida());
        response.setEmbalaje(domain.getEmbalaje());
        response.setActivo(domain.getActivo());
        response.setCreatedAt(domain.getCreatedAt());
        response.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getProductoBase() != null) {
            response.setIdProductoBase(domain.getProductoBase().getIdProducto());
        }

        return response;
    }
}
