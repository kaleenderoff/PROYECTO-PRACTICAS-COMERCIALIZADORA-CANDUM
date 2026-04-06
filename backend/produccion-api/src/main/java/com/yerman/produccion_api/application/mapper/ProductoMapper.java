package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ProductoRequest;
import com.yerman.produccion_api.application.dto.response.ProductoResponse;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Producto;

public class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toDomain(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setGramajeG(request.getGramajeG());
        producto.setMarca(request.getMarca());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setActivo(request.getActivo());

        LineaProduccion linea = new LineaProduccion();
        linea.setIdLineaProduccion(request.getIdLineaProduccion());
        producto.setLineaProduccion(linea);

        return producto;
    }

    public static ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setIdProducto(producto.getIdProducto());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setGramajeG(producto.getGramajeG());
        response.setMarca(producto.getMarca());
        response.setUnidadMedida(producto.getUnidadMedida());
        response.setActivo(producto.getActivo());
        response.setCreatedAt(producto.getCreatedAt());
        response.setUpdatedAt(producto.getUpdatedAt());

        if (producto.getLineaProduccion() != null) {
            response.setIdLineaProduccion(producto.getLineaProduccion().getIdLineaProduccion());
            response.setNombreLineaProduccion(producto.getLineaProduccion().getNombre());
        }

        return response;
    }
}
