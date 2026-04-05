package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.LineaProduccionRequest;
import com.yerman.produccion_api.application.dto.response.LineaProduccionResponse;
import com.yerman.produccion_api.domain.model.LineaProduccion;

public class LineaProduccionMapper {

    private LineaProduccionMapper() {
    }

    public static LineaProduccion toDomain(LineaProduccionRequest request) {
        LineaProduccion linea = new LineaProduccion();
        linea.setNombre(request.getNombre());
        linea.setDescripcion(request.getDescripcion());
        linea.setActivo(request.getActivo());
        return linea;
    }

    public static LineaProduccionResponse toResponse(LineaProduccion linea) {
        LineaProduccionResponse response = new LineaProduccionResponse();
        response.setIdLineaProduccion(linea.getIdLineaProduccion());
        response.setNombre(linea.getNombre());
        response.setDescripcion(linea.getDescripcion());
        response.setActivo(linea.getActivo());
        response.setCreatedAt(linea.getCreatedAt());
        response.setUpdatedAt(linea.getUpdatedAt());
        return response;
    }
}