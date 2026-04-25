package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.OrdenProduccionMapper;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.*;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class OrdenProduccionJpaAdapter implements OrdenProduccionRepositoryPort {

    private final OrdenProduccionJpaRepository repository;
    private final EntityManager entityManager;

    public OrdenProduccionJpaAdapter(
            OrdenProduccionJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public OrdenProduccion guardar(OrdenProduccion ordenProduccion) {
        OrdenProduccionEntity entity = toEntity(ordenProduccion);
        OrdenProduccionEntity guardada = repository.save(entity);
        return OrdenProduccionMapper.toDomain(guardada);
    }

    @Override
    public Optional<OrdenProduccion> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(OrdenProduccionMapper::toDomain);
    }

    @Override
    public List<OrdenProduccion> listarTodas() {
        return repository.findAll()
                .stream()
                .map(OrdenProduccionMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrdenProduccion> listarPorFecha(LocalDate fechaProduccion) {
        return repository.findByFechaProduccion(fechaProduccion)
                .stream()
                .map(OrdenProduccionMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorProgramacion(Long idProgramacion) {
        return repository.existsByProgramacionId(idProgramacion);
    }

    @Override
    public Optional<OrdenProduccion> obtenerPorNumeroOrden(String numeroOrden) {
        return repository.findByNumeroOrden(numeroOrden)
                .map(OrdenProduccionMapper::toDomain);
    }

    private OrdenProduccionEntity toEntity(OrdenProduccion orden) {
        OrdenProduccionEntity entity = new OrdenProduccionEntity();

        entity.setNumeroOrden(orden.getNumeroOrden());
        entity.setProgramacion(
                entityManager.getReference(ProgramacionProduccionEntity.class, orden.getIdProgramacion()));
        entity.setLinea(entityManager.getReference(CatalogoLineaEntity.class, orden.getIdLinea()));
        entity.setProducto(entityManager.getReference(CatalogoProductoEntity.class, orden.getIdProducto()));
        entity.setTurno(entityManager.getReference(TurnoEntity.class, orden.getIdTurno()));

        if (orden.getIdJefeLineaEjecutor() != null) {
            entity.setJefeLineaEjecutor(
                    entityManager.getReference(UsuarioEntity.class, orden.getIdJefeLineaEjecutor()));
        }

        entity.setCreadaPor(entityManager.getReference(UsuarioEntity.class, orden.getIdCreadaPor()));
        entity.setFechaProduccion(orden.getFechaProduccion());
        entity.setEstado(orden.getEstado());
        entity.setObservaciones(orden.getObservaciones());
        entity.setFechaInicioReal(orden.getFechaInicioReal());
        entity.setFechaFinReal(orden.getFechaFinReal());

        return entity;
    }
}