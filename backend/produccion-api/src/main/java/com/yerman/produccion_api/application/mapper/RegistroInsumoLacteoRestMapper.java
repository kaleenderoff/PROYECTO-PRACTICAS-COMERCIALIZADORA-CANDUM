package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.RegistroInsumoLacteoRequest;
import com.yerman.produccion_api.application.dto.response.RegistroInsumoLacteoResponse;
import com.yerman.produccion_api.domain.model.RegistroInsumoLacteo;

public class RegistroInsumoLacteoRestMapper {

    private RegistroInsumoLacteoRestMapper() {
    }

    public static RegistroInsumoLacteo toDomain(RegistroInsumoLacteoRequest request) {
        if (request == null) {
            return null;
        }

        RegistroInsumoLacteo registro = new RegistroInsumoLacteo();
        registro.setIdProduccionLactea(request.getIdProduccionLactea());
        registro.setIdProduccionLacteaBatch(request.getIdProduccionLacteaBatch());
        registro.setIdInsumo(request.getIdInsumo());
        registro.setLoteInsumo(request.getLoteInsumo());
        registro.setCantidadRequerida(request.getCantidadRequerida());
        registro.setCantidadUsada(request.getCantidadUsada());
        registro.setUnidadMedida(request.getUnidadMedida());
        registro.setFechaHoraRegistro(request.getFechaHoraRegistro());
        registro.setIdUsuario(request.getIdUsuario());
        registro.setObservaciones(request.getObservaciones());

        return registro;
    }

    public static RegistroInsumoLacteoResponse toResponse(RegistroInsumoLacteo registro) {
        if (registro == null) {
            return null;
        }

        return new RegistroInsumoLacteoResponse(
                registro.getId(),
                registro.getIdProduccionLactea(),
                registro.getIdProduccionLacteaBatch(),
                registro.getIdInsumo(),
                registro.getLoteInsumo(),
                registro.getCantidadRequerida(),
                registro.getCantidadUsada(),
                registro.getUnidadMedida(),
                registro.getFechaHoraRegistro(),
                registro.getIdUsuario(),
                registro.getObservaciones());
    }
}
