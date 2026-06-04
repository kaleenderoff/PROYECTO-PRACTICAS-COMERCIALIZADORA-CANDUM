package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.EstadoProgramacionProduccion;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE ProgramacionProduccionEntity p SET p.estado = :estado WHERE p.id = :id AND p.estado != :estado")
    void actualizarEstado(@Param("id") Long id, @Param("estado") com.yerman.produccion_api.domain.model.EstadoProgramacionProduccion estado);

    @Query(value = """
            SELECT COALESCE(SUM(p.num_baches_plan * p.kg_bache_plan), 0)
            FROM orden_produccion op
            JOIN programacion_produccion p ON op.id_programacion = p.id
            WHERE op.fecha_produccion = :fecha
              AND op.estado IN ('PROGRAMADA', 'EN_EJECUCION')
            """, nativeQuery = true)
    BigDecimal calcularLecheReservadaPorFecha(@Param("fecha") LocalDate fecha);
}