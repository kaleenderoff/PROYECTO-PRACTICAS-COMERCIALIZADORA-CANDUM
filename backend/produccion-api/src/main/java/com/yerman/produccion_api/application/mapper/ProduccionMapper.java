package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.ProduccionRequest;
import com.yerman.produccion_api.application.dto.response.ProduccionResponse;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Produccion;

public class ProduccionMapper {

    public static Produccion toDomain(ProduccionRequest request) {
        Produccion p = new Produccion();

        p.setFechaProduccion(request.getFechaProduccion());
        p.setTipoTurno(request.getTipoTurno());
        p.setNumeroLote(request.getNumeroLote());
        p.setEstado(request.getEstado());
        p.setObservacionesGenerales(request.getObservacionesGenerales());

        LineaProduccion linea = new LineaProduccion();
        linea.setIdLineaProduccion(request.getIdLineaProduccion());

        p.setLineaProduccion(linea);
        p.setIdOperario(request.getIdOperario());
        p.setIdJefeLinea(request.getIdJefeLinea());

        return p;
    }

    public static ProduccionResponse toResponse(Produccion p) {
        ProduccionResponse r = new ProduccionResponse();

        r.setIdProduccion(p.getIdProduccion());
        r.setFechaProduccion(p.getFechaProduccion());
        r.setTipoTurno(p.getTipoTurno());
        r.setNumeroLote(p.getNumeroLote());
        r.setEstado(p.getEstado());

        if (p.getLineaProduccion() != null) {
            r.setIdLineaProduccion(p.getLineaProduccion().getIdLineaProduccion());
        }

        r.setIdOperario(p.getIdOperario());
        r.setIdJefeLinea(p.getIdJefeLinea());
        r.setCreatedAt(p.getCreatedAt());

        return r;
    }
}
