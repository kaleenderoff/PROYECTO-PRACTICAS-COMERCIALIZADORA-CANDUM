package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.RecepcionLecheMapper;
import com.yerman.produccion_api.domain.model.RecepcionLechePesaje;
import com.yerman.produccion_api.domain.port.out.RecepcionLechePesajeRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLechePesajeEntity;
import com.yerman.produccion_api.infrastructure.repository.RecepcionLechePesajeJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecepcionLechePesajeJpaAdapter implements RecepcionLechePesajeRepositoryPort {

    private final RecepcionLechePesajeJpaRepository repository;
    private final EntityManager entityManager;

    public RecepcionLechePesajeJpaAdapter(
            RecepcionLechePesajeJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public RecepcionLechePesaje guardar(RecepcionLechePesaje pesaje) {
        RecepcionLechePesajeEntity entity = toEntity(pesaje);
        RecepcionLechePesajeEntity guardado = repository.save(entity);
        return RecepcionLecheMapper.toDomainPesaje(guardado);
    }

    @Override
    public List<RecepcionLechePesaje> listarPorRecepcion(Long idRecepcionLeche) {
        return repository.findByRecepcionLecheIdOrderByNumeroPesajeAsc(idRecepcionLeche)
                .stream()
                .map(RecepcionLecheMapper::toDomainPesaje)
                .toList();
    }

    private RecepcionLechePesajeEntity toEntity(RecepcionLechePesaje pesaje) {
        RecepcionLechePesajeEntity entity = new RecepcionLechePesajeEntity();

        entity.setId(pesaje.getId());
        entity.setRecepcionLeche(entityManager.getReference(
                RecepcionLecheEntity.class,
                pesaje.getIdRecepcionLeche()));
        entity.setNumeroPesaje(pesaje.getNumeroPesaje());
        entity.setPesoBrutoKg(pesaje.getPesoBrutoKg());
        entity.setTaraKg(pesaje.getTaraKg());
        entity.setPesoNetoKg(pesaje.getPesoNetoKg());
        entity.setObservaciones(pesaje.getObservaciones());

        return entity;
    }
}