package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionVsEmpaqueProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

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
}