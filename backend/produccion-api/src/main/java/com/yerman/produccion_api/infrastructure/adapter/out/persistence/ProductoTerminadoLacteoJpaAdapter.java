package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.ProductoTerminadoLacteoMapper;
import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoLacteoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;
import com.yerman.produccion_api.infrastructure.repository.ProductoTerminadoLacteoJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductoTerminadoLacteoJpaAdapter implements ProductoTerminadoLacteoRepositoryPort {

    private final ProductoTerminadoLacteoJpaRepository repository;
    private final EntityManager entityManager;

    public ProductoTerminadoLacteoJpaAdapter(
            ProductoTerminadoLacteoJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public ProductoTerminadoLacteo guardar(ProductoTerminadoLacteo productoTerminado) {
        ProductoTerminadoLacteoEntity entity = toEntity(productoTerminado);
        ProductoTerminadoLacteoEntity guardado = repository.save(entity);
        return ProductoTerminadoLacteoMapper.toDomain(guardado);
    }

    @Override
    public Optional<ProductoTerminadoLacteo> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(ProductoTerminadoLacteoMapper::toDomain);
    }

    @Override
    public List<ProductoTerminadoLacteo> listarTodos() {
        return repository.findAll()
                .stream()
                .map(ProductoTerminadoLacteoMapper::toDomain)
                .toList();
    }

    @Override
    public List<ProductoTerminadoLacteo> listarPorEstado(EstadoProductoTerminadoLacteo estado) {
        return repository.findByEstadoOrderByIdDesc(estado)
                .stream()
                .map(ProductoTerminadoLacteoMapper::toDomain)
                .toList();
    }

    @Override
    public List<ProductoTerminadoLacteo> listarPorProducto(String producto) {
        return repository.findByProductoContainingIgnoreCaseOrderByIdDesc(producto)
                .stream()
                .map(ProductoTerminadoLacteoMapper::toDomain)
                .toList();
    }

    private ProductoTerminadoLacteoEntity toEntity(ProductoTerminadoLacteo productoTerminado) {
        ProductoTerminadoLacteoEntity entity = new ProductoTerminadoLacteoEntity();

        entity.setId(productoTerminado.getId());
        entity.setProducto(productoTerminado.getProducto());
        entity.setLote(productoTerminado.getLote());
        entity.setKilosProducidos(productoTerminado.getKilosProducidos());
        entity.setKilosDisponibles(productoTerminado.getKilosDisponibles());
        entity.setEstado(productoTerminado.getEstado());
        entity.setObservaciones(productoTerminado.getObservaciones());

        entity.setProduccionLacteaBatch(entityManager.getReference(
                ProduccionLacteaBatchEntity.class,
                productoTerminado.getIdProduccionLacteaBatch()));

        return entity;
    }
}