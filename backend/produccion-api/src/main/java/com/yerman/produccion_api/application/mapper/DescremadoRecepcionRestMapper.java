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
        descremado.setIdTanqueDestino(request.getIdTanqueDestino());
        descremado.setLitrosDescremados(request.getLitrosDescremados());
        descremado.setCremaObtenidaKg(request.getCremaObtenidaKg());
        descremado.setIdSkuCrema(request.getIdSkuCrema());
        descremado.setUnidadesCrema(request.getUnidadesCrema());
        descremado.setKgPorUnidadCrema(request.getKgPorUnidadCrema());
        descremado.setLoteCrema(request.getLoteCrema());
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
                descremado.getIdTanqueDestino(),
                descremado.getLitrosDescremados(),
                descremado.getCremaObtenidaKg(),
                descremado.getIdSkuCrema(),
                descremado.getUnidadesCrema(),
                descremado.getKgPorUnidadCrema(),
                descremado.getLoteCrema(),
                descremado.getIdMovimientoSalida(),
                descremado.getIdMovimientoEntrada(),
                descremado.getObservaciones());
    }
}
