package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionFiltro;
import com.yerman.produccion_api.domain.port.out.ProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.LineaProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.ProduccionJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ProduccionJpaAdapter implements ProduccionRepositoryPort {

    private final ProduccionJpaRepository repository;

    public ProduccionJpaAdapter(ProduccionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Produccion guardar(Produccion produccion) {
        ProduccionEntity entity = toEntity(produccion);
        ProduccionEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Produccion> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Produccion> buscarPorNumeroLote(String numeroLote) {
        return repository.findByNumeroLote(numeroLote).map(this::toDomain);
    }

    @Override
    public boolean existePorNumeroLote(String numeroLote) {
        return repository.existsByNumeroLote(numeroLote);
    }

    @Override
    public List<Produccion> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Produccion> listarPorFecha(LocalDate fechaProduccion) {
        return repository.findByFechaProduccion(fechaProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Produccion> listarPorEstado(String estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Produccion> listarPorLineaProduccion(Long idLineaProduccion) {
        return repository.findByLineaProduccion_IdLineaProduccion(idLineaProduccion)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Produccion> listarPorOperario(Long idOperario) {
        return repository.findByOperario_IdUsuario(idOperario)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Produccion> listarPorJefeLinea(Long idJefeLinea) {
        return repository.findByJefeLinea_IdUsuario(idJefeLinea)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Page<Produccion> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Produccion> filtrar(ProduccionFiltro filtro, Pageable pageable) {
        return repository.filtrar(
                filtro.getNumeroLote(),
                filtro.getEstado(),
                filtro.getFechaDesde(),
                filtro.getFechaHasta(),
                filtro.getIdLineaProduccion(),
                filtro.getIdOperario(),
                filtro.getIdJefeLinea(),
                pageable).map(this::toDomain);
    }

    private Produccion toDomain(ProduccionEntity entity) {
        Produccion produccion = new Produccion();
        produccion.setIdProduccion(entity.getIdProduccion());
        produccion.setFechaProduccion(entity.getFechaProduccion());
        produccion.setTipoTurno(entity.getTipoTurno());
        produccion.setNumeroLote(entity.getNumeroLote());
        produccion.setFechaVencimiento(entity.getFechaVencimiento());
        produccion.setEstado(entity.getEstado());
        produccion.setObservacionesGenerales(entity.getObservacionesGenerales());
        produccion.setCreatedAt(entity.getCreatedAt());
        produccion.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getLineaProduccion() != null) {
            produccion.setLineaProduccion(toLineaDomain(entity.getLineaProduccion()));
        }

        if (entity.getOperario() != null) {
            produccion.setIdOperario(entity.getOperario().getIdUsuario());
        }

        if (entity.getJefeLinea() != null) {
            produccion.setIdJefeLinea(entity.getJefeLinea().getIdUsuario());
        }

        return produccion;
    }

    private ProduccionEntity toEntity(Produccion produccion) {
        ProduccionEntity entity = new ProduccionEntity();
        entity.setIdProduccion(produccion.getIdProduccion());
        entity.setFechaProduccion(produccion.getFechaProduccion());
        entity.setTipoTurno(produccion.getTipoTurno());
        entity.setNumeroLote(produccion.getNumeroLote());
        entity.setFechaVencimiento(produccion.getFechaVencimiento());
        entity.setEstado(produccion.getEstado());
        entity.setObservacionesGenerales(produccion.getObservacionesGenerales());

        if (produccion.getLineaProduccion() != null) {
            entity.setLineaProduccion(toLineaEntity(produccion.getLineaProduccion()));
        }

        if (produccion.getIdOperario() != null) {
            UsuarioEntity operario = new UsuarioEntity();
            operario.setIdUsuario(produccion.getIdOperario());
            entity.setOperario(operario);
        }

        if (produccion.getIdJefeLinea() != null) {
            UsuarioEntity jefeLinea = new UsuarioEntity();
            jefeLinea.setIdUsuario(produccion.getIdJefeLinea());
            entity.setJefeLinea(jefeLinea);
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

    private LineaProduccionEntity toLineaEntity(LineaProduccion linea) {
        LineaProduccionEntity entity = new LineaProduccionEntity();
        entity.setIdLineaProduccion(linea.getIdLineaProduccion());
        entity.setNombre(linea.getNombre());
        entity.setDescripcion(linea.getDescripcion());
        entity.setActivo(linea.getActivo());
        return entity;
    }
}