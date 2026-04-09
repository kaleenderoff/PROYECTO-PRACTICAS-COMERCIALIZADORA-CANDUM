package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.model.ProductoTerminado;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.ProductoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoEntity;
import com.yerman.produccion_api.infrastructure.repository.ProductoTerminadoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductoTerminadoJpaAdapter implements ProductoTerminadoRepositoryPort {

    private final ProductoTerminadoJpaRepository repository;

    public ProductoTerminadoJpaAdapter(ProductoTerminadoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductoTerminado guardar(ProductoTerminado productoTerminado) {
        ProductoTerminadoEntity entity = toEntity(productoTerminado);
        ProductoTerminadoEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ProductoTerminado> obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<ProductoTerminado> obtenerPorSku(String sku) {
        return repository.findBySku(sku).map(this::toDomain);
    }

    @Override
    public List<ProductoTerminado> listarActivos() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoTerminado> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorSku(String sku) {
        return repository.existsBySku(sku);
    }

    private ProductoTerminado toDomain(ProductoTerminadoEntity entity) {
        if (entity == null) {
            return null;
        }

        Producto productoBase = null;
        if (entity.getProducto() != null) {
            productoBase = new Producto();
            productoBase.setIdProducto(entity.getProducto().getIdProducto());
            productoBase.setNombre(entity.getProducto().getNombre());
            productoBase.setDescripcion(entity.getProducto().getDescripcion());
            productoBase.setGramajeG(entity.getProducto().getGramajeG());
            productoBase.setMarca(entity.getProducto().getMarca());
            productoBase.setUnidadMedida(entity.getProducto().getUnidadMedida());
            productoBase.setActivo(entity.getProducto().getActivo());
            productoBase.setCreatedAt(entity.getProducto().getCreatedAt());
            productoBase.setUpdatedAt(entity.getProducto().getUpdatedAt());
        }

        ProductoTerminado domain = new ProductoTerminado();
        domain.setId(entity.getId());
        domain.setProductoBase(productoBase);
        domain.setSku(entity.getSku());
        domain.setNombreComercial(entity.getNombreComercial());
        domain.setReferencia(entity.getReferencia());
        domain.setGramajeG(entity.getGramajeG());
        domain.setUnidadMedida(entity.getUnidadMedida());
        domain.setEmbalaje(entity.getEmbalaje());
        domain.setActivo(entity.getActivo());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }

    private ProductoTerminadoEntity toEntity(ProductoTerminado domain) {
        if (domain == null) {
            return null;
        }

        ProductoTerminadoEntity entity = new ProductoTerminadoEntity();
        entity.setId(domain.getId());
        entity.setSku(domain.getSku());
        entity.setNombreComercial(domain.getNombreComercial());
        entity.setReferencia(domain.getReferencia());
        entity.setGramajeG(domain.getGramajeG());
        entity.setUnidadMedida(domain.getUnidadMedida());
        entity.setEmbalaje(domain.getEmbalaje());
        entity.setActivo(domain.getActivo());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getProductoBase() != null && domain.getProductoBase().getIdProducto() != null) {
            ProductoEntity productoEntity = new ProductoEntity();
            productoEntity.setIdProducto(domain.getProductoBase().getIdProducto());
            entity.setProducto(productoEntity);
        }

        return entity;
    }
}
