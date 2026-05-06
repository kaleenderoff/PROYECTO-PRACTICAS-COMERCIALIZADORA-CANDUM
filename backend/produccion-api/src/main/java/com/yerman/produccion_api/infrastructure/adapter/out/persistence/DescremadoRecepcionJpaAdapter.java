package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.DescremadoRecepcionMapper;
import com.yerman.produccion_api.domain.model.DescremadoRecepcion;
import com.yerman.produccion_api.domain.port.out.DescremadoRecepcionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity;
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
        if (descremadoRecepcion.getIdTanqueDestino() != null) {
            entity.setTanqueDestino(entityManager.getReference(
                    TanqueLecheEntity.class,
                    descremadoRecepcion.getIdTanqueDestino()));
        }
        entity.setLitrosDescremados(descremadoRecepcion.getLitrosDescremados());
        entity.setCremaObtenidaKg(descremadoRecepcion.getCremaObtenidaKg());
        if (descremadoRecepcion.getIdSkuCrema() != null) {
            entity.setSkuCrema(entityManager.getReference(
                    CatalogoSkuEntity.class,
                    descremadoRecepcion.getIdSkuCrema()));
        }
        entity.setUnidadesCrema(descremadoRecepcion.getUnidadesCrema());
        entity.setKgPorUnidadCrema(descremadoRecepcion.getKgPorUnidadCrema());
        entity.setLoteCrema(descremadoRecepcion.getLoteCrema());
        if (descremadoRecepcion.getIdMovimientoSalida() != null) {
            entity.setMovimientoSalida(entityManager.getReference(
                    MovimientoLecheEntity.class,
                    descremadoRecepcion.getIdMovimientoSalida()));
        }
        if (descremadoRecepcion.getIdMovimientoEntrada() != null) {
            entity.setMovimientoEntrada(entityManager.getReference(
                    MovimientoLecheEntity.class,
                    descremadoRecepcion.getIdMovimientoEntrada()));
        }
        entity.setObservaciones(descremadoRecepcion.getObservaciones());

        return entity;
    }
}
