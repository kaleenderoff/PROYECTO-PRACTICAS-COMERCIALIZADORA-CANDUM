package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoLecheJpaRepository extends JpaRepository<MovimientoLecheEntity, Long> {

    Optional<MovimientoLecheEntity> findTopByTanqueIdOrderByFechaHoraDescIdDesc(Long idTanque);

    List<MovimientoLecheEntity> findByTanqueIdOrderByFechaHoraDescIdDesc(Long idTanque);

    List<MovimientoLecheEntity> findByFechaHoraBetweenOrderByFechaHoraDescIdDesc(
            LocalDateTime inicio,
            LocalDateTime fin);
}