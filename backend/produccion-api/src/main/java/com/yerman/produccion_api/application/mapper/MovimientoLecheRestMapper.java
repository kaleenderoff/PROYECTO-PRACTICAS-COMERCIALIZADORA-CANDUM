package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.MovimientoLecheResponse;
import com.yerman.produccion_api.domain.model.MovimientoLeche;

public class MovimientoLecheRestMapper {

    private MovimientoLecheRestMapper() {
    }

    public static MovimientoLecheResponse toResponse(MovimientoLeche m) {

        return new MovimientoLecheResponse(
                m.getId(),
                m.getIdTanque(),
                m.getTipoMovimiento().name(),
                m.getFechaHora(),
                m.getCantidadLitros(),
                m.getSaldoResultanteLitros());
    }
}