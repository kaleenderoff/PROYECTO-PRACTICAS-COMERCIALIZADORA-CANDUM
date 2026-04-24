package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgramacionProduccionJpaRepository extends JpaRepository<ProgramacionProduccionEntity, Long> {

    List<ProgramacionProduccionEntity> findByFechaProduccionOrderByIdDesc(LocalDate fechaProduccion);

    boolean existsByFechaProduccionAndLineaIdAndTurnoIdAndProductoId(
            LocalDate fechaProduccion,
            Long idLinea,
            Long idTurno,
            Long idProducto);

    Optional<ProgramacionProduccionEntity> findByCodigoProgramacion(String codigoProgramacion);
}