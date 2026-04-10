package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Empaque;
import com.yerman.produccion_api.domain.model.ProductoTerminado;
import com.yerman.produccion_api.domain.port.out.EmpaqueRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoEntity;
import com.yerman.produccion_api.infrastructure.repository.EmpaqueJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmpaqueJpaAdapter implements EmpaqueRepositoryPort {

    private final EmpaqueJpaRepository repository;

    public EmpaqueJpaAdapter(EmpaqueJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Empaque guardar(Empaque empaque) {
        EmpaqueEntity entity = toEntity(empaque);
        EmpaqueEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Empaque> obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Empaque> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Empaque> listarPorDetalleProduccion(Long idDetalleProduccion) {
        return repository.findByDetalleProduccion_IdDetalleProduccion(idDetalleProduccion)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Empaque> listarPorProductoTerminado(Long idProductoTerminado) {
        return repository.findByProductoTerminado_Id(idProductoTerminado)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Empaque> listarPorLoteEmpaque(String loteEmpaque) {
        return repository.findByLoteEmpaque(loteEmpaque)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Empaque> listarPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return repository.findByFechaEmpaqueBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Empaque toDomain(EmpaqueEntity entity) {
        if (entity == null) {
            return null;
        }

        DetalleProduccion detalleProduccion = null;
        if (entity.getDetalleProduccion() != null) {
            detalleProduccion = new DetalleProduccion();
            detalleProduccion.setIdDetalleProduccion(entity.getDetalleProduccion().getIdDetalleProduccion());
        }

        ProductoTerminado productoTerminado = null;
        if (entity.getProductoTerminado() != null) {
            productoTerminado = new ProductoTerminado();
            productoTerminado.setId(entity.getProductoTerminado().getId());
            productoTerminado.setSku(entity.getProductoTerminado().getSku());
            productoTerminado.setNombreComercial(entity.getProductoTerminado().getNombreComercial());
        }

        Empaque domain = new Empaque();
        domain.setId(entity.getId());
        domain.setDetalleProduccion(detalleProduccion);
        domain.setProductoTerminado(productoTerminado);
        domain.setLoteEmpaque(entity.getLoteEmpaque());
        domain.setFechaEmpaque(entity.getFechaEmpaque());
        domain.setFechaVencimiento(entity.getFechaVencimiento());
        domain.setEstado(entity.getEstado());
        domain.setCantidadUnidades(entity.getCantidadUnidades());
        domain.setCantidadCajas(entity.getCantidadCajas());
        domain.setPesoTotalKg(entity.getPesoTotalKg());
        domain.setObservaciones(entity.getObservaciones());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }

    private EmpaqueEntity toEntity(Empaque domain) {
        if (domain == null) {
            return null;
        }

        EmpaqueEntity entity = new EmpaqueEntity();
        entity.setId(domain.getId());
        entity.setLoteEmpaque(domain.getLoteEmpaque());
        entity.setFechaEmpaque(domain.getFechaEmpaque());
        entity.setFechaVencimiento(domain.getFechaVencimiento());
        entity.setEstado(domain.getEstado());
        entity.setCantidadUnidades(domain.getCantidadUnidades());
        entity.setCantidadCajas(domain.getCantidadCajas());
        entity.setPesoTotalKg(domain.getPesoTotalKg());
        entity.setObservaciones(domain.getObservaciones());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getDetalleProduccion() != null && domain.getDetalleProduccion().getIdDetalleProduccion() != null) {
            DetalleProduccionEntity detalleEntity = new DetalleProduccionEntity();
            detalleEntity.setIdDetalleProduccion(domain.getDetalleProduccion().getIdDetalleProduccion());
            entity.setDetalleProduccion(detalleEntity);
        }

        if (domain.getProductoTerminado() != null && domain.getProductoTerminado().getId() != null) {
            ProductoTerminadoEntity productoTerminadoEntity = new ProductoTerminadoEntity();
            productoTerminadoEntity.setId(domain.getProductoTerminado().getId());
            entity.setProductoTerminado(productoTerminadoEntity);
        }

        return entity;
    }
}
