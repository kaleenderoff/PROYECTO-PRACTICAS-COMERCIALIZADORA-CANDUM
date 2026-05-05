package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.ProduccionLacteaMapper;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionBatch;
import com.yerman.produccion_api.domain.port.out.ProduccionLacteaRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.MarmitaEntity;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaEntity;
import com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.ProduccionLacteaJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ProduccionLacteaJpaAdapter implements ProduccionLacteaRepositoryPort {

    private final ProduccionLacteaJpaRepository repository;
    private final EntityManager entityManager;

    public ProduccionLacteaJpaAdapter(
            ProduccionLacteaJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public Produccion guardar(Produccion produccion) {
        ProduccionLacteaEntity entity = toEntity(produccion);
        ProduccionLacteaEntity guardada = repository.save(entity);
        return ProduccionLacteaMapper.toDomain(guardada);
    }

    @Override
    public Optional<Produccion> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(ProduccionLacteaMapper::toDomain);
    }

    @Override
    public List<Produccion> listarTodas() {
        return repository.findAll()
                .stream()
                .map(ProduccionLacteaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Produccion> listarPorFecha(LocalDate fechaProduccion) {
        return repository.findByFechaProduccion(fechaProduccion)
                .stream()
                .map(ProduccionLacteaMapper::toDomain)
                .toList();
    }

    private ProduccionLacteaEntity toEntity(Produccion produccion) {
        ProduccionLacteaEntity entity = new ProduccionLacteaEntity();

        entity.setId(produccion.getId());
        entity.setFechaProduccion(produccion.getFechaProduccion());
        entity.setProducto(produccion.getProducto());
        entity.setObservaciones(produccion.getObservaciones());

        if (produccion.getIdOrdenProduccion() != null) {
            entity.setOrdenProduccion(entityManager.getReference(
                    OrdenProduccionEntity.class,
                    produccion.getIdOrdenProduccion()));
        }

        entity.setTanque(entityManager.getReference(
                TanqueLecheEntity.class,
                produccion.getIdTanque()));

        entity.setUsuario(entityManager.getReference(
                UsuarioEntity.class,
                produccion.getIdUsuario()));

        if (produccion.getBatches() != null) {
            List<ProduccionLacteaBatchEntity> batches = produccion.getBatches()
                    .stream()
                    .map(batch -> toBatchEntity(batch, entity))
                    .toList();

            entity.setBatches(batches);
        }

        return entity;
    }

    private ProduccionLacteaBatchEntity toBatchEntity(
            ProduccionBatch batch,
            ProduccionLacteaEntity produccionEntity) {

        ProduccionLacteaBatchEntity entity = new ProduccionLacteaBatchEntity();

        entity.setId(batch.getId());
        entity.setProduccion(produccionEntity);
        entity.setNumeroBatch(batch.getNumeroBatch());
        entity.setLitrosConsumidos(batch.getLitrosConsumidos());
        entity.setKilosProducidos(batch.getKilosProducidos());
        entity.setRendimiento(batch.getRendimiento());
        entity.setObservaciones(batch.getObservaciones());

        entity.setMarmita(entityManager.getReference(
                MarmitaEntity.class,
                batch.getIdMarmita()));

        if (batch.getIdMovimientoLeche() != null) {
            entity.setMovimientoLeche(entityManager.getReference(
                    MovimientoLecheEntity.class,
                    batch.getIdMovimientoLeche()));
        }

        return entity;
    }
}
