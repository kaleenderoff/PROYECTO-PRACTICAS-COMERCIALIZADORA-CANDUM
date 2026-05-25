package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrdenProduccionDetalleJpaRepository extends JpaRepository<OrdenProduccionDetalleEntity, Long> {

    List<OrdenProduccionDetalleEntity> findByOrdenId(Long idOrden);

    boolean existsByOrdenIdAndSkuId(Long idOrden, Long idSku);

    @Query("""
            SELECT d
            FROM OrdenProduccionDetalleEntity d
            JOIN FETCH d.orden o
            JOIN FETCH d.sku s
            JOIN FETCH s.producto p
            WHERE o.fechaProduccion = :fecha
              AND o.estado <> com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.CANCELADA
              AND d.unidadesReales IS NOT NULL
              AND d.unidadesReales > 0
            """)
    List<OrdenProduccionDetalleEntity> findProduccionRealParaReporteDiario(@Param("fecha") LocalDate fecha);
}
