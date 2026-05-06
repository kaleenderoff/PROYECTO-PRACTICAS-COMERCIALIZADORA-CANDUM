package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.RegistroInsumoLacteoMapper;
import com.yerman.produccion_api.domain.model.RegistroInsumoLacteo;
import com.yerman.produccion_api.domain.port.out.RegistroInsumoLacteoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.InsumoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaEntity;
import com.yerman.produccion_api.infrastructure.entity.RegistroInsumoLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.RegistroInsumoLacteoJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RegistroInsumoLacteoJpaAdapter implements RegistroInsumoLacteoRepositoryPort {

    private final RegistroInsumoLacteoJpaRepository repository;
    private final EntityManager entityManager;

    public RegistroInsumoLacteoJpaAdapter(
            RegistroInsumoLacteoJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public RegistroInsumoLacteo guardar(RegistroInsumoLacteo registro) {
        RegistroInsumoLacteoEntity entity = toEntity(registro);
        RegistroInsumoLacteoEntity guardado = repository.save(entity);
        return RegistroInsumoLacteoMapper.toDomain(guardado);
    }

    @Override
    public Optional<RegistroInsumoLacteo> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(RegistroInsumoLacteoMapper::toDomain);
    }

    @Override
    public List<RegistroInsumoLacteo> listarPorProduccion(Long idProduccionLactea) {
        return repository.findByProduccionLacteaIdOrderByFechaHoraRegistroDesc(idProduccionLactea)
                .stream()
                .map(RegistroInsumoLacteoMapper::toDomain)
                .toList();
    }

    @Override
    public List<RegistroInsumoLacteo> listarPorBatch(Long idProduccionLacteaBatch) {
        return repository.findByProduccionLacteaBatchIdOrderByFechaHoraRegistroDesc(idProduccionLacteaBatch)
                .stream()
                .map(RegistroInsumoLacteoMapper::toDomain)
                .toList();
    }

    private RegistroInsumoLacteoEntity toEntity(RegistroInsumoLacteo registro) {
        RegistroInsumoLacteoEntity entity = new RegistroInsumoLacteoEntity();

        entity.setId(registro.getId());
        entity.setLoteInsumo(registro.getLoteInsumo());
        entity.setCantidadRequerida(registro.getCantidadRequerida());
        entity.setCantidadUsada(registro.getCantidadUsada());
        entity.setUnidadMedida(registro.getUnidadMedida());
        entity.setFechaHoraRegistro(registro.getFechaHoraRegistro());
        entity.setObservaciones(registro.getObservaciones());

        entity.setProduccionLactea(entityManager.getReference(
                ProduccionLacteaEntity.class,
                registro.getIdProduccionLactea()));

        if (registro.getIdProduccionLacteaBatch() != null) {
            entity.setProduccionLacteaBatch(entityManager.getReference(
                    ProduccionLacteaBatchEntity.class,
                    registro.getIdProduccionLacteaBatch()));
        }

        entity.setInsumo(entityManager.getReference(
                InsumoEntity.class,
                registro.getIdInsumo()));

        entity.setUsuario(entityManager.getReference(
                UsuarioEntity.class,
                registro.getIdUsuario()));

        return entity;
    }
}
