package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch;
import com.yerman.produccion_api.infrastructure.entity.EjecucionBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EjecucionBatchRepository extends JpaRepository<EjecucionBatchEntity, Long> {
    
    List<EjecucionBatchEntity> findByOrdenProduccionId(Long idOrdenProduccion);
    
    Optional<EjecucionBatchEntity> findByOrdenProduccionIdAndNumeroBatch(Long idOrdenProduccion, Integer numeroBatch);
    
    @Query("SELECT COUNT(e) > 0 FROM EjecucionBatchEntity e WHERE e.marmita.id = :idMarmita AND e.estado = :estado AND e.ordenProduccion.id = :idOrden")
    boolean existsByMarmitaAndEstadoAndOrden(@Param("idMarmita") Long idMarmita, @Param("estado") EstadoBatch estado, @Param("idOrden") Long idOrden);

    @Query(value = """
            SELECT e.* FROM ejecucion_batch e
            JOIN orden_produccion op ON e.id_orden_produccion = op.id
            WHERE op.fecha_produccion = :fecha
              AND op.estado = 'FINALIZADA'
              AND e.estado IN ('FINALIZADO', 'CON_NOVEDAD')
              AND COALESCE(e.kg_producidos, 0) > 0
            """, nativeQuery = true)
    List<EjecucionBatchEntity> findBatchesFinalizadosPorFecha(@Param("fecha") java.time.LocalDate fecha);
}
