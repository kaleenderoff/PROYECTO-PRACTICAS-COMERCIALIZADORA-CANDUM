package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardResumenDetalleProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardValidacionPendienteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DetalleProduccionJpaRepository extends JpaRepository<DetalleProduccionEntity, Long> {

    List<DetalleProduccionEntity> findByProduccion_IdProduccion(Long idProduccion);

    List<DetalleProduccionEntity> findByProducto_IdProducto(Long idProducto);

    Optional<DetalleProduccionEntity> findByProduccion_IdProduccionAndProducto_IdProductoAndNumBatch(
            Long idProduccion,
            Long idProducto,
            Integer numBatch);

    boolean existsByProduccion_IdProduccionAndProducto_IdProductoAndNumBatch(
            Long idProduccion,
            Long idProducto,
            Integer numBatch);

    @Query("""
            select
                coalesce(sum(d.kgProgramados), 0) as totalKgProgramados,
                coalesce(sum(d.kgBatch), 0) as totalKgBatch,
                coalesce(sum(d.unidadesReales), 0) as totalUnidadesReales
            from DetalleProduccionEntity d
            """)
    DashboardResumenDetalleProjection obtenerResumenDetalle();

    @Query("""
            select
                d.idDetalleProduccion as idDetalleProduccion,
                p.idProduccion as idProduccion,
                p.numeroLote as numeroLote,
                p.fechaProduccion as fechaProduccion,
                p.estado as estadoProduccion,
                pr.idProducto as idProducto,
                pr.nombre as nombreProducto,
                d.numBatch as numBatch,
                d.kgProgramados as kgProgramados,
                d.kgBatch as kgBatch,
                d.unidadesReales as unidadesReales,
                d.rendimientoPct as rendimientoPct
            from DetalleProduccionEntity d
            join d.produccion p
            join d.producto pr
            left join d.validacion v
            where v is null
            """)
    List<DashboardValidacionPendienteProjection> obtenerValidacionesPendientesOptimizado();

    @Query("""
                select distinct d
                from DetalleProduccionEntity d
                join fetch d.producto pr
                join fetch d.produccion p
                left join fetch d.validacion v
                where p.idProduccion = :idProduccion
                order by d.numBatch asc
            """)
    List<DetalleProduccionEntity> findDetallesConProductoYValidacionPorProduccionId(Long idProduccion);
}