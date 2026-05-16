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
                    fecha_produccion AS fecha,
                    SUM(COALESCE(kg_producido_batches, 0)) AS totalProducido,
                    SUM(COALESCE(kg_pt_real, 0)) AS totalEmpaquetado
                FROM orden_produccion
                WHERE estado = 'FINALIZADA'
                GROUP BY fecha_produccion
                ORDER BY fecha_produccion DESC
            """, nativeQuery = true)
    List<DashboardProduccionVsEmpaqueProjection> obtenerProduccionVsEmpaque();

    @Query(value = """
                SELECT 
                    sku_descripcion AS sku,
                    peso_neto_gr AS pesoNeto,
                    SUM(unidades_reales) AS unidadesMes,
                    SUM(kg_pt_reales) AS kilosMes
                FROM reporte_produccion_diaria
                WHERE MONTH(fecha) = :mes AND YEAR(fecha) = :anio
                GROUP BY sku_descripcion, peso_neto_gr
            """, nativeQuery = true)
    List<Object[]> obtenerResumenSkuMensual(@Param("mes") int mes, @Param("anio") int anio);

    @Query(value = """
                SELECT 
                    (SELECT SUM(cantidad_recibida_litros) FROM recepcion_leche WHERE MONTH(fecha_recepcion) = :mes AND YEAR(fecha_recepcion) = :anio) as lecheRecibida,
                    (SELECT SUM(kg_pt_reales) FROM reporte_produccion_diaria WHERE MONTH(fecha) = :mes AND YEAR(fecha) = :anio AND tipo_producto = 'DL') as ptDulceLeche,
                    (SELECT SUM(kg_pt_reales) FROM reporte_produccion_diaria WHERE MONTH(fecha) = :mes AND YEAR(fecha) = :anio AND tipo_producto = 'LC') as ptLecheCondensada
            """, nativeQuery = true)
    List<Object[]> obtenerResumenMensualGerencial(@Param("mes") int mes, @Param("anio") int anio);

    @Query(value = """
                SELECT 
                    WEEK(fecha) as semana,
                    SUM(unidades_reales) as unidades,
                    SUM(kg_pt_reales) as kilos,
                    tipo_producto as tipo
                FROM reporte_produccion_diaria
                WHERE MONTH(fecha) = :mes AND YEAR(fecha) = :anio
                GROUP BY WEEK(fecha), tipo_producto
            """, nativeQuery = true)
    List<Object[]> obtenerDetalleSemanalGerencial(@Param("mes") int mes, @Param("anio") int anio);

    @Query(value = """
                SELECT 
                    MONTH(fecha) as mes,
                    SUM(CASE WHEN tipo_producto = 'DL' THEN kg_pt_reales ELSE 0 END) as ptDL,
                    SUM(CASE WHEN tipo_producto = 'LC' THEN kg_pt_reales ELSE 0 END) as ptLC
                FROM reporte_produccion_diaria
                WHERE YEAR(fecha) = :anio
                GROUP BY MONTH(fecha)
                ORDER BY MONTH(fecha)
            """, nativeQuery = true)
    List<Object[]> obtenerProduccionMensualAnual(@Param("anio") int anio);

    @Query(value = """
                SELECT 
                    MONTH(fecha_recepcion) as mes,
                    SUM(cantidad_recibida_litros) as leche
                FROM recepcion_leche
                WHERE YEAR(fecha_recepcion) = :anio
                GROUP BY MONTH(fecha_recepcion)
                ORDER BY MONTH(fecha_recepcion)
            """, nativeQuery = true)
    List<Object[]> obtenerRecepcionMensualAnual(@Param("anio") int anio);

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