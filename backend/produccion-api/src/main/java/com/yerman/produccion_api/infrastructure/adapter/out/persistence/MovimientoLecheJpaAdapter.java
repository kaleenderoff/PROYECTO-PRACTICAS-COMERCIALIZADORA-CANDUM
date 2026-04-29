package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.MovimientoLecheMapper;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.port.out.MovimientoLecheRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.MovimientoLecheJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class MovimientoLecheJpaAdapter implements MovimientoLecheRepositoryPort {

    private final MovimientoLecheJpaRepository repository;
    private final EntityManager entityManager;

    public MovimientoLecheJpaAdapter(
            MovimientoLecheJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public MovimientoLeche guardar(MovimientoLeche movimiento) {
        MovimientoLecheEntity entity = toEntity(movimiento);
        MovimientoLecheEntity guardado = repository.save(entity);
        return MovimientoLecheMapper.toDomain(guardado);
    }

    @Override
    public Optional<MovimientoLeche> obtenerUltimoMovimientoPorTanque(Long idTanque) {
        return repository.findTopByTanqueIdOrderByFechaHoraDescIdDesc(idTanque)
                .map(MovimientoLecheMapper::toDomain);
    }

    @Override
    public BigDecimal obtenerSaldoActualPorTanque(Long idTanque) {
        return obtenerUltimoMovimientoPorTanque(idTanque)
                .map(MovimientoLeche::getSaldoResultanteLitros)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public List<MovimientoLeche> listarPorTanque(Long idTanque) {
        return repository.findByTanqueIdOrderByFechaHoraDescIdDesc(idTanque)
                .stream()
                .map(MovimientoLecheMapper::toDomain)
                .toList();
    }

    @Override
    public List<MovimientoLeche> listarPorFecha(LocalDate fecha) {
        return repository.findByFechaHoraBetweenOrderByFechaHoraDescIdDesc(
                fecha.atStartOfDay(),
                fecha.plusDays(1).atStartOfDay())
                .stream()
                .map(MovimientoLecheMapper::toDomain)
                .toList();
    }

    private MovimientoLecheEntity toEntity(MovimientoLeche movimiento) {
        MovimientoLecheEntity entity = new MovimientoLecheEntity();

        entity.setTanque(entityManager.getReference(TanqueLecheEntity.class, movimiento.getIdTanque()));
        entity.setTipoMovimiento(movimiento.getTipoMovimiento());
        entity.setFechaHora(movimiento.getFechaHora());
        entity.setCantidadLitros(movimiento.getCantidadLitros());
        entity.setSaldoResultanteLitros(movimiento.getSaldoResultanteLitros());
        entity.setUsuario(entityManager.getReference(UsuarioEntity.class, movimiento.getIdUsuario()));
        entity.setReferencia(movimiento.getReferencia());
        entity.setObservaciones(movimiento.getObservaciones());

        return entity;
    }
}