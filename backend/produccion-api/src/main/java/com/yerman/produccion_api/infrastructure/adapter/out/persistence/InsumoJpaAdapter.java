package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.Insumo;
import com.yerman.produccion_api.domain.port.out.InsumoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.InsumoEntity;
import com.yerman.produccion_api.infrastructure.repository.InsumoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InsumoJpaAdapter implements InsumoRepositoryPort {

    private final InsumoJpaRepository repository;

    public InsumoJpaAdapter(InsumoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Insumo guardar(Insumo insumo) {
        InsumoEntity entity = toEntity(insumo);
        InsumoEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Insumo> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Insumo> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre).map(this::toDomain);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public List<Insumo> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Insumo> listarActivos() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Insumo toDomain(InsumoEntity entity) {
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

    private InsumoEntity toEntity(Insumo insumo) {
        InsumoEntity entity = new InsumoEntity();
        entity.setIdInsumo(insumo.getIdInsumo());
        entity.setNombre(insumo.getNombre());
        entity.setDescripcion(insumo.getDescripcion());
        entity.setUnidadMedida(insumo.getUnidadMedida());
        entity.setActivo(insumo.getActivo());
        return entity;
    }
}