package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.port.out.ProductoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.LineaProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoEntity;
import com.yerman.produccion_api.infrastructure.repository.ProductoJpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ProductoJpaAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository repository;

    public ProductoJpaAdapter(ProductoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = toEntity(producto);
        ProductoEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Producto> buscarPorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca) {
        return repository.findByNombreAndGramajeGAndMarca(nombre, gramajeG, marca)
                .map(this::toDomain);
    }

    @Override
    public boolean existePorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca) {
        return repository.existsByNombreAndGramajeGAndMarca(nombre, gramajeG, marca);
    }

    @Override
    public List<Producto> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Producto> listarActivos() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Producto> listarPorLineaProduccion(Long idLineaProduccion) {
        return repository.findByLineaProduccion_IdLineaProduccion(idLineaProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Producto> listarActivosPorLineaProduccion(Long idLineaProduccion) {
        return repository.findByLineaProduccion_IdLineaProduccionAndActivoTrue(idLineaProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Producto toDomain(ProductoEntity entity) {
        Producto producto = new Producto();
        producto.setIdProducto(entity.getIdProducto());
        producto.setNombre(entity.getNombre());
        producto.setDescripcion(entity.getDescripcion());
        producto.setGramajeG(entity.getGramajeG());
        producto.setMarca(entity.getMarca());
        producto.setUnidadMedida(entity.getUnidadMedida());
        producto.setActivo(entity.getActivo());
        producto.setCreatedAt(entity.getCreatedAt());
        producto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getLineaProduccion() != null) {
            producto.setLineaProduccion(toLineaDomain(entity.getLineaProduccion()));
        }

        return producto;
    }

    private ProductoEntity toEntity(Producto producto) {
        ProductoEntity entity = new ProductoEntity();
        entity.setIdProducto(producto.getIdProducto());
        entity.setNombre(producto.getNombre());
        entity.setDescripcion(producto.getDescripcion());
        entity.setGramajeG(producto.getGramajeG());
        entity.setMarca(producto.getMarca());
        entity.setUnidadMedida(producto.getUnidadMedida());
        entity.setActivo(producto.getActivo());

        if (producto.getLineaProduccion() != null) {
            LineaProduccionEntity linea = new LineaProduccionEntity();
            linea.setIdLineaProduccion(
                    producto.getLineaProduccion().getIdLineaProduccion());
            entity.setLineaProduccion(linea);
        }

        return entity;
    }

    private LineaProduccion toLineaDomain(LineaProduccionEntity entity) {
        LineaProduccion linea = new LineaProduccion();
        linea.setIdLineaProduccion(entity.getIdLineaProduccion());
        linea.setNombre(entity.getNombre());
        linea.setDescripcion(entity.getDescripcion());
        linea.setActivo(entity.getActivo());
        linea.setCreatedAt(entity.getCreatedAt());
        linea.setUpdatedAt(entity.getUpdatedAt());
        return linea;
    }
}
