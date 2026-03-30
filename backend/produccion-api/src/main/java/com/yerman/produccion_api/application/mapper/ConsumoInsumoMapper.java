package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ConsumoInsumoRequest;
import com.yerman.produccion_api.application.dto.response.ConsumoInsumoResponse;
import com.yerman.produccion_api.domain.model.ConsumoInsumo;
import com.yerman.produccion_api.domain.model.Insumo;

public class ConsumoInsumoMapper {

    public static ConsumoInsumo toDomain(ConsumoInsumoRequest request) {
        ConsumoInsumo consumo = new ConsumoInsumo();

        consumo.setIdProduccion(request.getIdProduccion());
        consumo.setIdDetalleProduccion(request.getIdDetalleProduccion());

        Insumo insumo = new Insumo();
        insumo.setIdInsumo(request.getIdInsumo());
        consumo.setInsumo(insumo);

        consumo.setCantidadConsumida(request.getCantidadConsumida());
        consumo.setObservaciones(request.getObservaciones());

        return consumo;
    }

    public static ConsumoInsumoResponse toResponse(ConsumoInsumo consumo) {
        ConsumoInsumoResponse response = new ConsumoInsumoResponse();

        response.setIdConsumoInsumo(consumo.getIdConsumoInsumo());
        response.setIdProduccion(consumo.getIdProduccion());
        response.setIdDetalleProduccion(consumo.getIdDetalleProduccion());

        if (consumo.getInsumo() != null) {
            response.setIdInsumo(consumo.getInsumo().getIdInsumo());
        }

        response.setCantidadConsumida(consumo.getCantidadConsumida());
        response.setObservaciones(consumo.getObservaciones());
        response.setCreatedAt(consumo.getCreatedAt());

        return response;
    }
}
