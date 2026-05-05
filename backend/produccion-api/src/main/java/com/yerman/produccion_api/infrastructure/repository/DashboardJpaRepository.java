package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionVsEmpaqueProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardTrazabilidadLoteProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DashboardJpaRepository extends Repository<ProductoTerminadoLacteoEntity, Long> {

    @Query(value = """
                SELECT
                    p.fecha_produccion AS fecha,
                    SUM(pt.kilos_producidos) AS totalProducido,
                    COALESCE(SUM(e.kilos_utilizados), 0) AS totalEmpaquetado
                FROM producto_terminado_lacteo pt
                JOIN produccion_lactea_batch b
                    ON pt.id_produccion_lactea_batch = b.id
                JOIN produccion_lactea p
                    ON b.id_produccion_lactea = p.id
                LEFT JOIN empaque_lacteo e
                    ON e.id_producto_terminado_lacteo = pt.id
                   AND e.estado <> 'ANULADO'
                GROUP BY p.fecha_produccion
                ORDER BY p.fecha_produccion DESC
            """, nativeQuery = true)
    List<DashboardProduccionVsEmpaqueProjection> obtenerProduccionVsEmpaque();

    @Query(value = """
                SELECT
                    pt.lote AS lote,
                    pt.producto AS producto,
                    p.fecha_produccion AS fechaProduccion,
                    b.numero_batch AS numeroBatch,
                    pt.kilos_producidos AS kilosProducidos,
                    pt.kilos_disponibles AS kilosDisponibles,
                    COALESCE(SUM(e.kilos_utilizados), 0) AS kilosEmpacados,
                    pt.estado AS estadoProductoTerminado
                FROM producto_terminado_lacteo pt
                JOIN produccion_lactea_batch b
                    ON pt.id_produccion_lactea_batch = b.id
                JOIN produccion_lactea p
                    ON b.id_produccion_lactea = p.id
                LEFT JOIN empaque_lacteo e
                    ON e.id_producto_terminado_lacteo = pt.id
                   AND e.estado <> 'ANULADO'
                WHERE pt.lote = :lote
                GROUP BY
                    pt.lote,
                    pt.producto,
                    p.fecha_produccion,
                    b.numero_batch,
                    pt.kilos_producidos,
                    pt.kilos_disponibles,
                    pt.estado
            """, nativeQuery = true)
    Optional<DashboardTrazabilidadLoteProjection> obtenerTrazabilidadPorLote(@Param("lote") String lote);
}