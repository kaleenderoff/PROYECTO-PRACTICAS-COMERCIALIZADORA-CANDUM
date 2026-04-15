package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.Validacion;
import com.yerman.produccion_api.domain.port.out.ValidacionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.entity.ValidacionEntity;
import com.yerman.produccion_api.infrastructure.repository.ValidacionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ValidacionJpaAdapter implements ValidacionRepositoryPort {

    private final ValidacionJpaRepository repository;

    public ValidacionJpaAdapter(ValidacionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Validacion guardar(Validacion validacion) {
        ValidacionEntity entity = toEntity(validacion);
        ValidacionEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Validacion> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Validacion> buscarPorDetalleProduccion(Long idDetalleProduccion) {
        return repository.findByDetalleProduccion_IdDetalleProduccion(idDetalleProduccion)
                .map(this::toDomain);
    }

    @Override
    public boolean existePorDetalleProduccion(Long idDetalleProduccion) {
        return repository.existsByDetalleProduccion_IdDetalleProduccion(idDetalleProduccion);
    }

    @Override
    public List<Validacion> listarTodas() {
        return repository.findAllConRelaciones()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Validacion> listarPorEstado(EstadoValidacion estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Validacion> listarPorValidador(Long idValidador) {
        return repository.findByValidador_IdUsuario(idValidador)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Validacion toDomain(ValidacionEntity entity) {
        Validacion validacion = new Validacion();
        validacion.setIdValidacion(entity.getIdValidacion());

        if (entity.getDetalleProduccion() != null) {
            validacion.setIdDetalleProduccion(entity.getDetalleProduccion().getIdDetalleProduccion());
        }

        if (entity.getValidador() != null) {
            validacion.setIdValidador(entity.getValidador().getIdUsuario());
        }

        validacion.setEstado(entity.getEstado());
        validacion.setObservacion(entity.getObservacion());
        validacion.setFechaValidacion(entity.getFechaValidacion());
        validacion.setCreatedAt(entity.getCreatedAt());
        validacion.setUpdatedAt(entity.getUpdatedAt());

        return validacion;
    }

    private ValidacionEntity toEntity(Validacion validacion) {
        ValidacionEntity entity = new ValidacionEntity();
        entity.setIdValidacion(validacion.getIdValidacion());

        if (validacion.getIdDetalleProduccion() != null) {
            DetalleProduccionEntity detalle = new DetalleProduccionEntity();
            detalle.setIdDetalleProduccion(validacion.getIdDetalleProduccion());
            entity.setDetalleProduccion(detalle);
        }

        if (validacion.getIdValidador() != null) {
            UsuarioEntity validador = new UsuarioEntity();
            validador.setIdUsuario(validacion.getIdValidador());
            entity.setValidador(validador);
        }

        entity.setEstado(validacion.getEstado());
        entity.setObservacion(validacion.getObservacion());
        entity.setFechaValidacion(validacion.getFechaValidacion());

        return entity;
    }
}