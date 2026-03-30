package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.port.out.LineaProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.LineaProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.LineaProduccionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LineaProduccionJpaAdapter implements LineaProduccionRepositoryPort {

    private final LineaProduccionJpaRepository repository;

    public LineaProduccionJpaAdapter(LineaProduccionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LineaProduccion guardar(LineaProduccion lineaProduccion) {
        LineaProduccionEntity entity = toEntity(lineaProduccion);
        LineaProduccionEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<LineaProduccion> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<LineaProduccion> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre).map(this::toDomain);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public List<LineaProduccion> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<LineaProduccion> listarActivas() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private LineaProduccion toDomain(LineaProduccionEntity entity) {
        LineaProduccion linea = new LineaProduccion();
        linea.setIdLineaProduccion(entity.getIdLineaProduccion());
        linea.setNombre(entity.getNombre());
        linea.setDescripcion(entity.getDescripcion());
        linea.setActivo(entity.getActivo());
        linea.setCreatedAt(entity.getCreatedAt());
        linea.setUpdatedAt(entity.getUpdatedAt());
        return linea;
    }

    private LineaProduccionEntity toEntity(LineaProduccion linea) {
        LineaProduccionEntity entity = new LineaProduccionEntity();
        entity.setIdLineaProduccion(linea.getIdLineaProduccion());
        entity.setNombre(linea.getNombre());
        entity.setDescripcion(linea.getDescripcion());
        entity.setActivo(linea.getActivo());
        return entity;
    }
}
