package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ReporteProduccionDiariaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReporteProduccionDiariaJpaRepository extends JpaRepository<ReporteProduccionDiariaEntity, Long> {

    List<ReporteProduccionDiariaEntity> findByFechaBetweenOrderByFechaAsc(LocalDate start, LocalDate end);

    @Query("SELECT r FROM ReporteProduccionDiariaEntity r WHERE r.fecha BETWEEN :start AND :end AND r.tipoProducto = :tipo ORDER BY r.fecha ASC")
    List<ReporteProduccionDiariaEntity> findByFechaBetweenAndTipo(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("tipo") ReporteProduccionDiariaEntity.TipoProducto tipo);
}
