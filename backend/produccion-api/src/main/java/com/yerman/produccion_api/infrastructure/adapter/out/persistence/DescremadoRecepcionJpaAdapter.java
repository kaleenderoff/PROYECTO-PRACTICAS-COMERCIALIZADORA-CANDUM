package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.DescremadoRecepcionMapper;
import com.yerman.produccion_api.domain.model.DescremadoRecepcion;
import com.yerman.produccion_api.domain.port.out.DescremadoRecepcionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.repository.DescremadoRecepcionJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DescremadoRecepcionJpaAdapter implements DescremadoRecepcionRepositoryPort {

    private final DescremadoRecepcionJpaRepository repository;
    private final EntityManager entityManager;

    public DescremadoRecepcionJpaAdapter(
            DescremadoRecepcionJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public DescremadoRecepcion guardar(DescremadoRecepcion descremadoRecepcion) {
        DescremadoRecepcionEntity entity = toEntity(descremadoRecepcion);
        DescremadoRecepcionEntity guardado = repository.save(entity);
        return DescremadoRecepcionMapper.toDomain(guardado);
    }

    @Override
    public Optional<DescremadoRecepcion> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(DescremadoRecepcionMapper::toDomain);
    }

    @Override
    public List<DescremadoRecepcion> listarTodos() {
        return repository.findAll()
                .stream()
                .map(DescremadoRecepcionMapper::toDomain)
                .toList();
    }

    @Override
    public List<DescremadoRecepcion> listarPorRecepcion(Long idRecepcionLeche) {
        return repository.findByRecepcionLecheIdOrderByIdDesc(idRecepcionLeche)
                .stream()
                .map(DescremadoRecepcionMapper::toDomain)
                .toList();
    }

    private DescremadoRecepcionEntity toEntity(DescremadoRecepcion descremadoRecepcion) {
        DescremadoRecepcionEntity entity = new DescremadoRecepcionEntity();

        entity.setId(descremadoRecepcion.getId());
        entity.setRecepcionLeche(entityManager.getReference(
                RecepcionLecheEntity.class,
                descremadoRecepcion.getIdRecepcionLeche()));
        entity.setLitrosDescremados(descremadoRecepcion.getLitrosDescremados());
        entity.setCremaObtenidaKg(descremadoRecepcion.getCremaObtenidaKg());
        entity.setObservaciones(descremadoRecepcion.getObservaciones());

        return entity;
    }
}