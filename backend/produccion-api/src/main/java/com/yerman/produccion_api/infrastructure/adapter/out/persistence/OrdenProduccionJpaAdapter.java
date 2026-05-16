package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.OrdenProduccionMapper;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.CatalogoLineaEntity;
import com.yerman.produccion_api.infrastructure.entity.CatalogoProductoEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.TurnoEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionJpaRepository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    public OrdenProduccion guardar(OrdenProduccion domain) {
        OrdenProduccionEntity entity;
        if (domain.getId() != null) {
            entity = repository.findById(domain.getId()).orElse(new OrdenProduccionEntity());
        } else {
            entity = new OrdenProduccionEntity();
        }
        
        mapToEntity(domain, entity);
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

    private void mapToEntity(OrdenProduccion domain, OrdenProduccionEntity entity) {
        entity.setId(domain.getId());
        entity.setNumeroOrden(domain.getNumeroOrden());
        entity.setProgramacion(entityManager.getReference(ProgramacionProduccionEntity.class, domain.getIdProgramacion()));
        entity.setLinea(entityManager.getReference(CatalogoLineaEntity.class, domain.getIdLinea()));
        entity.setProducto(entityManager.getReference(CatalogoProductoEntity.class, domain.getIdProducto()));
        entity.setTurno(entityManager.getReference(TurnoEntity.class, domain.getIdTurno()));

        if (domain.getIdJefeLineaEjecutor() != null) {
            entity.setJefeLineaEjecutor(entityManager.getReference(UsuarioEntity.class, domain.getIdJefeLineaEjecutor()));
        }

        entity.setCreadaPor(entityManager.getReference(UsuarioEntity.class, domain.getIdCreadaPor()));
        entity.setFechaProduccion(domain.getFechaProduccion());
        entity.setEstado(domain.getEstado());
        entity.setObservaciones(domain.getObservaciones());
        entity.setFechaInicioReal(domain.getFechaInicioReal());
        entity.setFechaFinReal(domain.getFechaFinReal());
        
        if (domain.getIdTanqueLeche() != null) {
            entity.setTanqueLeche(entityManager.getReference(com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity.class, domain.getIdTanqueLeche()));
        } else {
            entity.setTanqueLeche(null);
        }

        entity.setKgEntradaReal(domain.getKgEntradaReal());
        entity.setKgProducidoBatches(domain.getKgProducidoBatches());
        entity.setKgPtReal(domain.getKgPtReal());
        entity.setRendimientoReal(domain.getRendimientoReal());
        entity.setMermaReal(domain.getMermaReal());
        entity.setMermaEmpaque(domain.getMermaEmpaque());
    }
}