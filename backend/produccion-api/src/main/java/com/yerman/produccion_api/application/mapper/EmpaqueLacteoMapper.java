package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.EmpaqueLacteo;
import com.yerman.produccion_api.domain.model.EstadoEmpaqueLacteo;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;

public class EmpaqueLacteoMapper {

    private EmpaqueLacteoMapper() {
    }

    public static EmpaqueLacteo toDomain(EmpaqueLacteoEntity entity) {
        if (entity == null) {
            return null;
        }

        EmpaqueLacteo empaqueLacteo = new EmpaqueLacteo();

        empaqueLacteo.setId(entity.getId());

        if (entity.getProductoTerminadoLacteo() != null) {
            empaqueLacteo.setProductoTerminadoLacteoId(entity.getProductoTerminadoLacteo().getId());
        }

        empaqueLacteo.setLoteEmpaque(entity.getLoteEmpaque());
        empaqueLacteo.setFechaEmpaque(entity.getFechaEmpaque());
        empaqueLacteo.setFechaVencimiento(entity.getFechaVencimiento());
        empaqueLacteo.setKilosUtilizados(entity.getKilosUtilizados());
        empaqueLacteo.setUnidades(entity.getUnidades());
        empaqueLacteo.setCajas(entity.getCajas());
        empaqueLacteo.setPesoTotalKg(entity.getPesoTotalKg());

        if (entity.getEstado() != null && !entity.getEstado().isBlank()) {
            empaqueLacteo.setEstado(EstadoEmpaqueLacteo.valueOf(entity.getEstado().trim().toUpperCase()));
        }

        empaqueLacteo.setObservaciones(entity.getObservaciones());

        return empaqueLacteo;
    }

    public static EmpaqueLacteoEntity toEntity(EmpaqueLacteo domain) {
        if (domain == null) {
            return null;
        }

        EmpaqueLacteoEntity entity = new EmpaqueLacteoEntity();

        entity.setId(domain.getId());

        if (domain.getProductoTerminadoLacteoId() != null) {
            ProductoTerminadoLacteoEntity productoTerminadoLacteoEntity = new ProductoTerminadoLacteoEntity();
            productoTerminadoLacteoEntity.setId(domain.getProductoTerminadoLacteoId());
            entity.setProductoTerminadoLacteo(productoTerminadoLacteoEntity);
        }

        entity.setLoteEmpaque(domain.getLoteEmpaque());
        entity.setFechaEmpaque(domain.getFechaEmpaque());
        entity.setFechaVencimiento(domain.getFechaVencimiento());
        entity.setKilosUtilizados(domain.getKilosUtilizados());
        entity.setUnidades(domain.getUnidades());
        entity.setCajas(domain.getCajas());
        entity.setPesoTotalKg(domain.getPesoTotalKg());

        if (domain.getEstado() != null) {
            entity.setEstado(domain.getEstado().name());
        }

        entity.setObservaciones(domain.getObservaciones());

        return entity;
    }
}