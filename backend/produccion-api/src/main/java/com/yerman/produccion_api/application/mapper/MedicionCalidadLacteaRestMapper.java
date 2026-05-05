package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.MedicionCalidadLacteaRequest;
import com.yerman.produccion_api.application.dto.response.MedicionCalidadLacteaResponse;
import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;

public class MedicionCalidadLacteaRestMapper {

    private MedicionCalidadLacteaRestMapper() {
    }

    public static MedicionCalidadLactea toDomain(MedicionCalidadLacteaRequest request) {
        if (request == null) {
            return null;
        }

        MedicionCalidadLactea medicion = new MedicionCalidadLactea();
        medicion.setIdProduccionLactea(request.getIdProduccionLactea());
        medicion.setIdProduccionLacteaBatch(request.getIdProduccionLacteaBatch());
        medicion.setTipoMedicion(request.getTipoMedicion());
        medicion.setReferencia(request.getReferencia());
        medicion.setBrix(request.getBrix());
        medicion.setPh(request.getPh());
        medicion.setFechaHoraMedicion(request.getFechaHoraMedicion());
        medicion.setIdUsuarioCalidad(request.getIdUsuarioCalidad());
        medicion.setObservaciones(request.getObservaciones());

        return medicion;
    }

    public static MedicionCalidadLacteaResponse toResponse(MedicionCalidadLactea medicion) {
        if (medicion == null) {
            return null;
        }

        return new MedicionCalidadLacteaResponse(
                medicion.getId(),
                medicion.getIdProduccionLactea(),
                medicion.getIdProduccionLacteaBatch(),
                medicion.getTipoMedicion(),
                medicion.getReferencia(),
                medicion.getBrix(),
                medicion.getPh(),
                medicion.getFechaHoraMedicion(),
                medicion.getIdUsuarioCalidad(),
                medicion.getObservaciones());
    }
}
