package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.EjecucionBatch;
import com.yerman.produccion_api.infrastructure.entity.EjecucionBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.MarmitaEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;

public class EjecucionBatchMapper {

    private EjecucionBatchMapper() {
    }

    public static EjecucionBatch toDomain(EjecucionBatchEntity entity) {
        if (entity == null)
            return null;

        EjecucionBatch domain = new EjecucionBatch(
                entity.getId(),
                entity.getOrdenProduccion() != null ? entity.getOrdenProduccion().getId() : null,
                entity.getNumeroBatch(),
                entity.getMarmita() != null ? entity.getMarmita().getId() : null,
                entity.getMovimientoLeche() != null ? entity.getMovimientoLeche().getId() : null,
                entity.getKgEntrada(),
                entity.getKgProducidos(),
                entity.getRendimientoPct(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getHuboReproceso(),
                entity.getBatchConforme(),
                entity.getBrixFinal(),
                entity.getTipoNovedad(),
                entity.getFechaInicio(),
                entity.getFechaFin());

        if (entity.getMarmita() != null) {
            domain.setNombreMarmita(entity.getMarmita().getNombre());
        }

        if (entity.getMovimientoLeche() != null) {
            domain.setLitrosLecheDescontados(entity.getMovimientoLeche().getCantidadLitros());
            domain.setSaldoResultanteLecheLitros(entity.getMovimientoLeche().getSaldoResultanteLitros());
        }

        return domain;
    }

    public static EjecucionBatchEntity toEntity(
            EjecucionBatch domain,
            OrdenProduccionEntity orden,
            MarmitaEntity marmita) {

        if (domain == null)
            return null;

        EjecucionBatchEntity entity = new EjecucionBatchEntity();
        entity.setId(domain.getId());
        entity.setOrdenProduccion(orden);
        entity.setNumeroBatch(domain.getNumeroBatch());
        entity.setMarmita(marmita);
        entity.setKgEntrada(domain.getKgEntrada());
        entity.setKgProducidos(domain.getKgProducidos());
        entity.setRendimientoPct(domain.getRendimientoPct());
        entity.setEstado(domain.getEstado());
        entity.setObservaciones(domain.getObservaciones());
        entity.setHuboReproceso(domain.getHuboReproceso());
        entity.setBatchConforme(domain.getBatchConforme());
        entity.setBrixFinal(domain.getBrixFinal());
        entity.setTipoNovedad(domain.getTipoNovedad());
        entity.setFechaInicio(domain.getFechaInicio());
        entity.setFechaFin(domain.getFechaFin());

        return entity;
    }
}