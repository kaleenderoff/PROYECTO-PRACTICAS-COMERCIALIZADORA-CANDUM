package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.OrdenProduccionDetalleMapper;
import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionDetalleRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionDetalleEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionDetalleJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdenProduccionDetalleJpaAdapter implements OrdenProduccionDetalleRepositoryPort {

    private final OrdenProduccionDetalleJpaRepository repository;
    private final EntityManager entityManager;

    public OrdenProduccionDetalleJpaAdapter(
            OrdenProduccionDetalleJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public OrdenProduccionDetalle guardar(OrdenProduccionDetalle detalle) {
        OrdenProduccionDetalleEntity entity = toEntity(detalle);
        OrdenProduccionDetalleEntity guardada = repository.save(entity);
        return OrdenProduccionDetalleMapper.toDomain(guardada);
    }

    @Override
    public Optional<OrdenProduccionDetalle> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(OrdenProduccionDetalleMapper::toDomain);
    }

    @Override
    public List<OrdenProduccionDetalle> listarPorOrden(Long idOrden) {
        return repository.findByOrdenId(idOrden)
                .stream()
                .map(OrdenProduccionDetalleMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorOrdenYSku(Long idOrden, Long idSku) {
        return repository.existsByOrdenIdAndSkuId(idOrden, idSku);
    }

    @Override
    public void eliminarPorId(Long id) {
        repository.deleteById(id);
    }

    private OrdenProduccionDetalleEntity toEntity(OrdenProduccionDetalle detalle) {
        OrdenProduccionDetalleEntity entity = new OrdenProduccionDetalleEntity();

        entity.setOrden(entityManager.getReference(OrdenProduccionEntity.class, detalle.getIdOrden()));

        if (detalle.getIdProgramacionSku() != null) {
            entity.setProgramacionSku(
                    entityManager.getReference(ProgramacionSkuEntity.class, detalle.getIdProgramacionSku()));
        }

        entity.setSku(entityManager.getReference(CatalogoSkuEntity.class, detalle.getIdSku()));
        entity.setCantidadProgramada(detalle.getCantidadProgramada());
        entity.setUnidadProgramada(detalle.getUnidadProgramada());
        entity.setPrioridad(detalle.getPrioridad());
        entity.setObservaciones(detalle.getObservaciones());

        return entity;
    }
}