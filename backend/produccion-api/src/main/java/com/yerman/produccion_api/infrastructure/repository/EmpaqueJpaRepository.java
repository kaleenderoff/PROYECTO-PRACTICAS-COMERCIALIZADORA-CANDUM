package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.EmpaqueEntity;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionPorSkuProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionVsEmpaqueProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardResumenEmpaqueProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmpaqueJpaRepository extends JpaRepository<EmpaqueEntity, Long> {

    List<EmpaqueEntity> findByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    List<EmpaqueEntity> findByProductoTerminado_Id(Long idProductoTerminado);

    List<EmpaqueEntity> findByFechaEmpaqueBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<EmpaqueEntity> findByLoteEmpaque(String loteEmpaque);

    @Query("""
            select e
            from EmpaqueEntity e
            join fetch e.productoTerminado pt
            join fetch e.detalleProduccion dp
            order by e.fechaEmpaque desc
            """)
    List<EmpaqueEntity> findAllConProductoTerminadoYDetalle();

    @Query("""
            select
                coalesce(sum(e.cantidadUnidades), 0) as totalUnidadesEmpacadas,
                coalesce(sum(e.cantidadCajas), 0) as totalCajasEmpacadas,
                coalesce(sum(e.pesoTotalKg), 0) as totalPesoEmpacadoKg
            from EmpaqueEntity e
            """)
    DashboardResumenEmpaqueProjection obtenerResumenEmpaque();

    @Query("""
            select
                pt.id as idProductoTerminado,
                pt.sku as sku,
                pt.nombreComercial as nombreComercial,
                pt.referencia as referencia,
                coalesce(sum(e.cantidadUnidades), 0) as totalUnidades,
                coalesce(sum(e.cantidadCajas), 0) as totalCajas,
                coalesce(sum(e.pesoTotalKg), 0) as totalPesoKg,
                count(e.id) as totalRegistrosEmpaque
            from EmpaqueEntity e
            join e.productoTerminado pt
            group by pt.id, pt.sku, pt.nombreComercial, pt.referencia
            order by pt.nombreComercial asc
            """)
    List<DashboardProduccionPorSkuProjection> obtenerProduccionPorSkuOptimizado();

    @Query("""
            select
                d.idDetalleProduccion as idDetalleProduccion,
                p.idProduccion as idProduccion,
                p.numeroLote as numeroLoteProduccion,
                pr.idProducto as idProducto,
                pr.nombre as nombreProducto,
                d.numBatch as numBatch,
                d.kgProgramados as kgProgramados,
                d.kgBatch as kgBatch,
                d.unidadesReales as unidadesReales,
                coalesce(sum(e.cantidadUnidades), 0) as unidadesEmpacadas,
                coalesce(sum(e.cantidadCajas), 0) as cajasEmpacadas,
                coalesce(sum(e.pesoTotalKg), 0) as pesoEmpacadoKg
            from DetalleProduccionEntity d
            join d.produccion p
            join d.producto pr
            left join EmpaqueEntity e on e.detalleProduccion.idDetalleProduccion = d.idDetalleProduccion
            group by
                d.idDetalleProduccion,
                p.idProduccion,
                p.numeroLote,
                pr.idProducto,
                pr.nombre,
                d.numBatch,
                d.kgProgramados,
                d.kgBatch,
                d.unidadesReales
            order by p.numeroLote asc, d.numBatch asc
            """)
    List<DashboardProduccionVsEmpaqueProjection> obtenerProduccionVsEmpaqueOptimizado();

    @Query("""
            select e
            from EmpaqueEntity e
            join fetch e.productoTerminado pt
            where e.detalleProduccion.idDetalleProduccion in :idsDetalleProduccion
            order by e.fechaEmpaque asc
            """)
    List<EmpaqueEntity> findByDetalleProduccionIdsConProductoTerminado(List<Long> idsDetalleProduccion);
}