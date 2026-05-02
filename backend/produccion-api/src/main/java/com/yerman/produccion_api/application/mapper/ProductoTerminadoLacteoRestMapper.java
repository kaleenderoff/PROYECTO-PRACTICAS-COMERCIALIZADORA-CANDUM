package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ProductoTerminadoLacteoRequest;
import com.yerman.produccion_api.application.dto.response.ProductoTerminadoLacteoResponse;
import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;

public class ProductoTerminadoLacteoRestMapper {

    private ProductoTerminadoLacteoRestMapper() {
    }

    public static ProductoTerminadoLacteo toDomain(ProductoTerminadoLacteoRequest request) {
        if (request == null) {
            return null;
        }

        ProductoTerminadoLacteo productoTerminado = new ProductoTerminadoLacteo();
        productoTerminado.setIdProduccionLacteaBatch(request.getIdProduccionLacteaBatch());
        productoTerminado.setProducto(request.getProducto());
        productoTerminado.setLote(request.getLote());
        productoTerminado.setKilosProducidos(request.getKilosProducidos());
        productoTerminado.setKilosDisponibles(request.getKilosDisponibles());
        productoTerminado.setObservaciones(request.getObservaciones());

        return productoTerminado;
    }

    public static ProductoTerminadoLacteoResponse toResponse(ProductoTerminadoLacteo productoTerminado) {
        if (productoTerminado == null) {
            return null;
        }

        return new ProductoTerminadoLacteoResponse(
                productoTerminado.getId(),
                productoTerminado.getIdProduccionLacteaBatch(),
                productoTerminado.getProducto(),
                productoTerminado.getLote(),
                productoTerminado.getKilosProducidos(),
                productoTerminado.getKilosDisponibles(),
                productoTerminado.getEstado(),
                productoTerminado.getObservaciones());
    }
}