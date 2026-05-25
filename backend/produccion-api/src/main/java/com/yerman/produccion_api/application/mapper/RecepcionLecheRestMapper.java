package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.RecepcionLechePesajeRequest;
import com.yerman.produccion_api.application.dto.request.RecepcionLecheRequest;
import com.yerman.produccion_api.application.dto.response.RecepcionLechePesajeResponse;
import com.yerman.produccion_api.application.dto.response.RecepcionLecheResponse;
import com.yerman.produccion_api.domain.model.RecepcionLeche;
import com.yerman.produccion_api.domain.model.RecepcionLechePesaje;

import java.util.List;

public class RecepcionLecheRestMapper {

    private RecepcionLecheRestMapper() {
    }

    public static RecepcionLeche toDomain(RecepcionLecheRequest request) {
        if (request == null) {
            return null;
        }

        List<RecepcionLechePesaje> pesajes = request.getPesajes() == null
                ? List.of()
                : request.getPesajes()
                        .stream()
                        .map(RecepcionLecheRestMapper::toDomainPesaje)
                        .toList();

        RecepcionLeche recepcion = new RecepcionLeche();
        recepcion.setFechaRecepcion(request.getFechaRecepcion());
        recepcion.setTipoMateriaPrima(request.getTipoMateriaPrima());
        recepcion.setProveedor(request.getProveedor());
        recepcion.setCantidadRecibidaLitros(request.getCantidadRecibidaLitros());
        recepcion.setRecibidoPor(request.getRecibidoPor());
        recepcion.setIdUsuario(request.getIdUsuario());
        recepcion.setIdTanque(request.getIdTanque());
        recepcion.setIdMovimientoLeche(null);
        recepcion.setNumeroRemision(request.getNumeroRemision());
        recepcion.setCantidadRemisionLitros(request.getCantidadRemisionLitros());
        recepcion.setObservaciones(request.getObservaciones());
        recepcion.setPesajes(pesajes);

        return recepcion;
    }

    private static RecepcionLechePesaje toDomainPesaje(RecepcionLechePesajeRequest request) {
        if (request == null) {
            return null;
        }

        RecepcionLechePesaje pesaje = new RecepcionLechePesaje();
        pesaje.setNumeroPesaje(request.getNumeroPesaje());
        pesaje.setPesoBrutoKg(request.getPesoBrutoKg());
        pesaje.setTaraKg(request.getTaraKg());
        pesaje.setObservaciones(request.getObservaciones());

        return pesaje;
    }

    public static RecepcionLecheResponse toResponse(RecepcionLeche recepcion) {
        if (recepcion == null) {
            return null;
        }

        List<RecepcionLechePesajeResponse> pesajes = recepcion.getPesajes() == null
                ? List.of()
                : recepcion.getPesajes()
                        .stream()
                        .map(RecepcionLecheRestMapper::toResponsePesaje)
                        .toList();

        return new RecepcionLecheResponse(
                recepcion.getId(),
                recepcion.getFechaRecepcion(),
                recepcion.getTipoMateriaPrima(),
                recepcion.getProveedor(),
                recepcion.getCantidadRecibidaLitros(),
                recepcion.getRecibidoPor(),
                recepcion.getIdTanque(),
                recepcion.getIdUsuario(),
                recepcion.getIdMovimientoLeche(),
                recepcion.getNumeroRemision(),
                recepcion.getCantidadRemisionLitros(),
                recepcion.getObservaciones(),
                pesajes);
    }

    private static RecepcionLechePesajeResponse toResponsePesaje(RecepcionLechePesaje pesaje) {
        if (pesaje == null) {
            return null;
        }

        return new RecepcionLechePesajeResponse(
                pesaje.getId(),
                pesaje.getIdRecepcionLeche(),
                pesaje.getNumeroPesaje(),
                pesaje.getPesoBrutoKg(),
                pesaje.getTaraKg(),
                pesaje.getPesoNetoKg(),
                pesaje.getObservaciones());
    }
}