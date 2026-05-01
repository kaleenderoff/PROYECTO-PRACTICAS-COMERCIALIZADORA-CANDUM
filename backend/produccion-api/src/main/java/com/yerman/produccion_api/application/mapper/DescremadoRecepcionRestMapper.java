package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.DescremadoRecepcionRequest;
import com.yerman.produccion_api.application.dto.response.DescremadoRecepcionResponse;
import com.yerman.produccion_api.domain.model.DescremadoRecepcion;

public class DescremadoRecepcionRestMapper {

    private DescremadoRecepcionRestMapper() {
    }

    public static DescremadoRecepcion toDomain(DescremadoRecepcionRequest request) {
        if (request == null) {
            return null;
        }

        DescremadoRecepcion descremado = new DescremadoRecepcion();
        descremado.setIdRecepcionLeche(request.getIdRecepcionLeche());
        descremado.setLitrosDescremados(request.getLitrosDescremados());
        descremado.setCremaObtenidaKg(request.getCremaObtenidaKg());
        descremado.setObservaciones(request.getObservaciones());

        return descremado;
    }

    public static DescremadoRecepcionResponse toResponse(DescremadoRecepcion descremado) {
        if (descremado == null) {
            return null;
        }

        return new DescremadoRecepcionResponse(
                descremado.getId(),
                descremado.getIdRecepcionLeche(),
                descremado.getLitrosDescremados(),
                descremado.getCremaObtenidaKg(),
                descremado.getObservaciones());
    }
}