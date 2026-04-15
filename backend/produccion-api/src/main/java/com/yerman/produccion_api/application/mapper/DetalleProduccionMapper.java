package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.DetalleProduccionRequest;
import com.yerman.produccion_api.application.dto.response.DetalleProduccionResponse;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Producto;

public class DetalleProduccionMapper {

    private DetalleProduccionMapper() {
    }

    public static DetalleProduccion toDomain(DetalleProduccionRequest request) {
        DetalleProduccion detalle = new DetalleProduccion();

        detalle.setIdProduccion(request.getIdProduccion());

        Producto producto = new Producto();
        producto.setIdProducto(request.getIdProducto());
        detalle.setProducto(producto);

        detalle.setKgProgramados(request.getKgProgramados());
        detalle.setKgBatch(request.getKgBatch());
        detalle.setNumBatch(request.getNumBatch());
        detalle.setUnidadesReales(request.getUnidadesReales());
        detalle.setObservaciones(request.getObservaciones());

        return detalle;
    }

    public static DetalleProduccionResponse toResponse(DetalleProduccion detalle) {
        DetalleProduccionResponse response = new DetalleProduccionResponse();

        response.setIdDetalleProduccion(detalle.getIdDetalleProduccion());
        response.setIdProduccion(detalle.getIdProduccion());
        response.setNumeroLoteProduccion(detalle.getNumeroLoteProduccion());
        response.setFechaProduccion(detalle.getFechaProduccion());
        response.setEstadoProduccion(detalle.getEstadoProduccion());

        if (detalle.getProducto() != null) {
            response.setIdProducto(detalle.getProducto().getIdProducto());
            response.setNombreProducto(detalle.getProducto().getNombre());
        }

        response.setKgProgramados(detalle.getKgProgramados());
        response.setKgBatch(detalle.getKgBatch());
        response.setNumBatch(detalle.getNumBatch());
        response.setUnidadesReales(detalle.getUnidadesReales());
        response.setRendimientoPct(detalle.getRendimientoPct());
        response.setObservaciones(detalle.getObservaciones());
        response.setFechaHoraRegistro(detalle.getFechaHoraRegistro());
        response.setTieneValidacion(detalle.getTieneValidacion());

        return response;
    }
}