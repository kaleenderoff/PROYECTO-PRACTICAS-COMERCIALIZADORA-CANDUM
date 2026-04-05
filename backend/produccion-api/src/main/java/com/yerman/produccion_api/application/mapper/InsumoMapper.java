package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.InsumoRequest;
import com.yerman.produccion_api.application.dto.response.InsumoResponse;
import com.yerman.produccion_api.domain.model.Insumo;

public class InsumoMapper {

    private InsumoMapper() {
    }

    public static Insumo toDomain(InsumoRequest request) {
        Insumo insumo = new Insumo();
        insumo.setNombre(request.getNombre());
        insumo.setDescripcion(request.getDescripcion());
        insumo.setUnidadMedida(request.getUnidadMedida());
        insumo.setActivo(request.getActivo());
        return insumo;
    }

    public static InsumoResponse toResponse(Insumo insumo) {
        InsumoResponse response = new InsumoResponse();
        response.setIdInsumo(insumo.getIdInsumo());
        response.setNombre(insumo.getNombre());
        response.setDescripcion(insumo.getDescripcion());
        response.setUnidadMedida(insumo.getUnidadMedida());
        response.setActivo(insumo.getActivo());
        response.setCreatedAt(insumo.getCreatedAt());
        response.setUpdatedAt(insumo.getUpdatedAt());
        return response;
    }
}
