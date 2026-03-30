package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.ConsumoInsumo;
import com.yerman.produccion_api.domain.model.Insumo;
import com.yerman.produccion_api.domain.port.out.ConsumoInsumoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.ConsumoInsumoEntity;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.InsumoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.ConsumoInsumoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsumoInsumoJpaAdapter implements ConsumoInsumoRepositoryPort {

    private final ConsumoInsumoJpaRepository repository;

    public ConsumoInsumoJpaAdapter(ConsumoInsumoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ConsumoInsumo guardar(ConsumoInsumo consumoInsumo) {
        ConsumoInsumoEntity entity = toEntity(consumoInsumo);
        ConsumoInsumoEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ConsumoInsumo> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ConsumoInsumo> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ConsumoInsumo> listarPorProduccion(Long idProduccion) {
        return repository.findByProduccion_IdProduccion(idProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ConsumoInsumo> listarPorDetalleProduccion(Long idDetalleProduccion) {
        return repository.findByDetalleProduccion_IdDetalleProduccion(idDetalleProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ConsumoInsumo> listarPorInsumo(Long idInsumo) {
        return repository.findByInsumo_IdInsumo(idInsumo)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<ConsumoInsumo> buscarPorProduccionInsumoDetalle(Long idProduccion, Long idInsumo,
            Long idDetalleProduccion) {
        return repository.findByProduccion_IdProduccionAndInsumo_IdInsumoAndDetalleProduccion_IdDetalleProduccion(
                idProduccion, idInsumo, idDetalleProduccion).map(this::toDomain);
    }

    @Override
    public boolean existePorProduccionInsumoDetalle(Long idProduccion, Long idInsumo, Long idDetalleProduccion) {
        return repository.existsByProduccion_IdProduccionAndInsumo_IdInsumoAndDetalleProduccion_IdDetalleProduccion(
                idProduccion, idInsumo, idDetalleProduccion);
    }

    private ConsumoInsumo toDomain(ConsumoInsumoEntity entity) {
        ConsumoInsumo consumo = new ConsumoInsumo();
        consumo.setIdConsumoInsumo(entity.getIdConsumoInsumo());

        if (entity.getProduccion() != null) {
            consumo.setIdProduccion(entity.getProduccion().getIdProduccion());
        }

        if (entity.getDetalleProduccion() != null) {
            consumo.setIdDetalleProduccion(entity.getDetalleProduccion().getIdDetalleProduccion());
        }

        if (entity.getInsumo() != null) {
            consumo.setInsumo(toInsumoDomain(entity.getInsumo()));
        }

        consumo.setCantidadConsumida(entity.getCantidadConsumida());
        consumo.setObservaciones(entity.getObservaciones());
        consumo.setCreatedAt(entity.getCreatedAt());
        consumo.setUpdatedAt(entity.getUpdatedAt());

        return consumo;
    }

    private ConsumoInsumoEntity toEntity(ConsumoInsumo consumo) {
        ConsumoInsumoEntity entity = new ConsumoInsumoEntity();
        entity.setIdConsumoInsumo(consumo.getIdConsumoInsumo());

        if (consumo.getIdProduccion() != null) {
            ProduccionEntity produccion = new ProduccionEntity();
            produccion.setIdProduccion(consumo.getIdProduccion());
            entity.setProduccion(produccion);
        }

        if (consumo.getIdDetalleProduccion() != null) {
            DetalleProduccionEntity detalle = new DetalleProduccionEntity();
            detalle.setIdDetalleProduccion(consumo.getIdDetalleProduccion());
            entity.setDetalleProduccion(detalle);
        }

        if (consumo.getInsumo() != null) {
            entity.setInsumo(toInsumoEntity(consumo.getInsumo()));
        }

        entity.setCantidadConsumida(consumo.getCantidadConsumida());
        entity.setObservaciones(consumo.getObservaciones());

        return entity;
    }

    private Insumo toInsumoDomain(InsumoEntity entity) {
        Insumo insumo = new Insumo();
        insumo.setIdInsumo(entity.getIdInsumo());
        insumo.setNombre(entity.getNombre());
        insumo.setDescripcion(entity.getDescripcion());
        insumo.setUnidadMedida(entity.getUnidadMedida());
        insumo.setActivo(entity.getActivo());
        insumo.setCreatedAt(entity.getCreatedAt());
        insumo.setUpdatedAt(entity.getUpdatedAt());
        return insumo;
    }

    private InsumoEntity toInsumoEntity(Insumo insumo) {
        InsumoEntity entity = new InsumoEntity();
        entity.setIdInsumo(insumo.getIdInsumo());
        entity.setNombre(insumo.getNombre());
        entity.setDescripcion(insumo.getDescripcion());
        entity.setUnidadMedida(insumo.getUnidadMedida());
        entity.setActivo(insumo.getActivo());
        return entity;
    }
}
