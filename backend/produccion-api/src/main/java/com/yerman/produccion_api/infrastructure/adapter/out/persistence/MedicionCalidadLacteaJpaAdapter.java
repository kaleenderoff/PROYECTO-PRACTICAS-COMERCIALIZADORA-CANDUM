package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.MedicionCalidadLacteaMapper;
import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;
import com.yerman.produccion_api.domain.model.TipoMedicionCalidadLactea;
import com.yerman.produccion_api.domain.port.out.MedicionCalidadLacteaRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.EjecucionBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.MedicionCalidadLacteaEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.MedicionCalidadLacteaJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MedicionCalidadLacteaJpaAdapter implements MedicionCalidadLacteaRepositoryPort {

    private final MedicionCalidadLacteaJpaRepository repository;
    private final EntityManager entityManager;

    public MedicionCalidadLacteaJpaAdapter(
            MedicionCalidadLacteaJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public MedicionCalidadLactea guardar(MedicionCalidadLactea medicion) {
        MedicionCalidadLacteaEntity entity = toEntity(medicion);
        MedicionCalidadLacteaEntity guardada = repository.save(entity);
        return MedicionCalidadLacteaMapper.toDomain(guardada);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<MedicionCalidadLactea> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(MedicionCalidadLacteaMapper::toDomain);
    }

    @Override
    public List<MedicionCalidadLactea> listarPorProduccion(Long idProduccionLactea) {
        return repository.findByProduccionLacteaIdOrderByFechaHoraMedicionDesc(idProduccionLactea)
                .stream()
                .map(MedicionCalidadLacteaMapper::toDomain)
                .toList();
    }

    @Override
    public List<MedicionCalidadLactea> listarPorOrden(Long idOrdenProduccion) {
        return repository.findByOrdenProduccionIdOrderByFechaHoraMedicionDesc(idOrdenProduccion)
                .stream()
                .map(MedicionCalidadLacteaMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeMedicionPorOrdenBatchYTipo(
            Long idOrdenProduccion,
            Long idEjecucionBatch,
            TipoMedicionCalidadLactea tipoMedicion) {

        if (idOrdenProduccion == null || idEjecucionBatch == null || tipoMedicion == null) {
            return false;
        }

        return repository.existsByOrdenProduccionIdAndEjecucionBatchIdAndTipoMedicion(
                idOrdenProduccion,
                idEjecucionBatch,
                tipoMedicion);
    }

    private MedicionCalidadLacteaEntity toEntity(MedicionCalidadLactea medicion) {
        MedicionCalidadLacteaEntity entity = new MedicionCalidadLacteaEntity();

        entity.setId(medicion.getId());
        entity.setTipoMedicion(medicion.getTipoMedicion());
        entity.setReferencia(medicion.getReferencia());
        entity.setBrix(medicion.getBrix());
        entity.setPh(medicion.getPh());
        entity.setFechaHoraMedicion(medicion.getFechaHoraMedicion());
        entity.setObservaciones(medicion.getObservaciones());

        if (medicion.getIdProduccionLactea() != null) {
            entity.setProduccionLactea(entityManager.getReference(
                    ProduccionLacteaEntity.class,
                    medicion.getIdProduccionLactea()));
        }

        if (medicion.getIdProduccionLacteaBatch() != null) {
            entity.setProduccionLacteaBatch(entityManager.getReference(
                    ProduccionLacteaBatchEntity.class,
                    medicion.getIdProduccionLacteaBatch()));
        }

        if (medicion.getIdOrdenProduccion() != null) {
            entity.setOrdenProduccion(entityManager.getReference(
                    OrdenProduccionEntity.class,
                    medicion.getIdOrdenProduccion()));
        }

        if (medicion.getIdEjecucionBatch() != null) {
            entity.setEjecucionBatch(entityManager.getReference(
                    EjecucionBatchEntity.class,
                    medicion.getIdEjecucionBatch()));
        }

        entity.setUsuarioCalidad(entityManager.getReference(
                UsuarioEntity.class,
                medicion.getIdUsuarioCalidad()));

        return entity;
    }
}