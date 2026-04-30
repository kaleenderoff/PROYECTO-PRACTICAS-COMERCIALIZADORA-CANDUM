package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.RecepcionLeche;
import com.yerman.produccion_api.domain.model.RecepcionLechePesaje;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLechePesajeEntity;

import java.util.List;

public class RecepcionLecheMapper {

    private RecepcionLecheMapper() {
    }

    public static RecepcionLeche toDomain(RecepcionLecheEntity entity) {
        if (entity == null) {
            return null;
        }

        List<RecepcionLechePesaje> pesajes = entity.getPesajes() == null
                ? List.of()
                : entity.getPesajes()
                        .stream()
                        .map(RecepcionLecheMapper::toDomainPesaje)
                        .toList();

        return new RecepcionLeche(
                entity.getId(),
                entity.getFechaRecepcion(),
                entity.getTipoMateriaPrima(),
                entity.getProveedor(),
                entity.getCantidadRecibidaLitros(),
                entity.getRecibidoPor(),
                entity.getTanque() != null ? entity.getTanque().getId() : null,
                entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null,
                entity.getMovimientoLeche() != null ? entity.getMovimientoLeche().getId() : null,
                entity.getNumeroRemision(),
                entity.getCantidadRemisionLitros(),
                entity.getObservaciones(),
                pesajes);
    }

    public static RecepcionLechePesaje toDomainPesaje(RecepcionLechePesajeEntity entity) {
        if (entity == null) {
            return null;
        }

        return new RecepcionLechePesaje(
                entity.getId(),
                entity.getRecepcionLeche() != null ? entity.getRecepcionLeche().getId() : null,
                entity.getNumeroPesaje(),
                entity.getPesoBrutoKg(),
                entity.getTaraKg(),
                entity.getPesoNetoKg(),
                entity.getObservaciones());
    }
}