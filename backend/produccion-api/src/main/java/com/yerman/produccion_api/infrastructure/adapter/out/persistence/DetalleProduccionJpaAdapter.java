package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.port.out.DetalleProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.DetalleProduccionJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class DetalleProduccionJpaAdapter implements DetalleProduccionRepositoryPort {

    private final DetalleProduccionJpaRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public DetalleProduccionJpaAdapter(DetalleProduccionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public DetalleProduccion guardar(DetalleProduccion detalleProduccion) {
        DetalleProduccionEntity entity = toEntity(detalleProduccion);

        DetalleProduccionEntity saved = repository.saveAndFlush(entity);

        entityManager.refresh(saved);

        return toDomain(saved);
    }

    @Override
    public Optional<DetalleProduccion> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<DetalleProduccion> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<DetalleProduccion> listarPorProduccion(Long idProduccion) {
        return repository.findDetallesConProductoYValidacionPorProduccionId(idProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<DetalleProduccion> listarPorProducto(Long idProducto) {
        return repository.findByProducto_IdProducto(idProducto)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<DetalleProduccion> buscarPorProduccionProductoBatch(Long idProduccion, Long idProducto,
            Integer numBatch) {
        return repository
                .findByProduccion_IdProduccionAndProducto_IdProductoAndNumBatch(idProduccion, idProducto, numBatch)
                .map(this::toDomain);
    }

    @Override
    public boolean existePorProduccionProductoBatch(Long idProduccion, Long idProducto, Integer numBatch) {
        return repository.existsByProduccion_IdProduccionAndProducto_IdProductoAndNumBatch(idProduccion, idProducto,
                numBatch);
    }

    private DetalleProduccion toDomain(DetalleProduccionEntity entity) {
        DetalleProduccion detalle = new DetalleProduccion();
        detalle.setIdDetalleProduccion(entity.getIdDetalleProduccion());

        if (entity.getProduccion() != null) {
            detalle.setIdProduccion(entity.getProduccion().getIdProduccion());
            detalle.setNumeroLoteProduccion(entity.getProduccion().getNumeroLote());
            detalle.setFechaProduccion(entity.getProduccion().getFechaProduccion());
            detalle.setEstadoProduccion(entity.getProduccion().getEstado());
        }

        if (entity.getProducto() != null) {
            detalle.setProducto(toProductoDomain(entity.getProducto()));
        }

        detalle.setKgProgramados(entity.getKgProgramados());
        detalle.setKgBatch(entity.getKgBatch());
        detalle.setNumBatch(entity.getNumBatch());
        detalle.setUnidadesReales(entity.getUnidadesReales());
        detalle.setRendimientoPct(entity.getRendimientoPct());
        detalle.setObservaciones(entity.getObservaciones());
        detalle.setFechaHoraRegistro(entity.getFechaHoraRegistro());
        detalle.setCreatedAt(entity.getCreatedAt());
        detalle.setUpdatedAt(entity.getUpdatedAt());
        detalle.setTieneValidacion(entity.getValidacion() != null);

        return detalle;
    }

    private DetalleProduccionEntity toEntity(DetalleProduccion detalle) {
        DetalleProduccionEntity entity = new DetalleProduccionEntity();
        entity.setIdDetalleProduccion(detalle.getIdDetalleProduccion());

        if (detalle.getIdProduccion() != null) {
            ProduccionEntity produccion = new ProduccionEntity();
            produccion.setIdProduccion(detalle.getIdProduccion());
            entity.setProduccion(produccion);
        }

        if (detalle.getProducto() != null) {
            entity.setProducto(toProductoEntity(detalle.getProducto()));
        }

        entity.setKgProgramados(detalle.getKgProgramados());
        entity.setKgBatch(detalle.getKgBatch());
        entity.setNumBatch(detalle.getNumBatch());
        entity.setUnidadesReales(detalle.getUnidadesReales());
        entity.setObservaciones(detalle.getObservaciones());

        return entity;
    }

    private Producto toProductoDomain(ProductoEntity entity) {
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
        return producto;
    }

    private ProductoEntity toProductoEntity(Producto producto) {
        ProductoEntity entity = new ProductoEntity();
        entity.setIdProducto(producto.getIdProducto());
        entity.setNombre(producto.getNombre());
        entity.setDescripcion(producto.getDescripcion());
        entity.setGramajeG(producto.getGramajeG());
        entity.setMarca(producto.getMarca());
        entity.setUnidadMedida(producto.getUnidadMedida());
        entity.setActivo(producto.getActivo());
        return entity;
    }
}
