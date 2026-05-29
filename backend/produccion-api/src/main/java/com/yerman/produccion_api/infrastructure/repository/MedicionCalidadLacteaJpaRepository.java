package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.TipoMedicionCalidadLactea;
import com.yerman.produccion_api.infrastructure.entity.MedicionCalidadLacteaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicionCalidadLacteaJpaRepository extends JpaRepository<MedicionCalidadLacteaEntity, Long> {

    List<MedicionCalidadLacteaEntity> findByProduccionLacteaIdOrderByFechaHoraMedicionDesc(Long idProduccionLactea);

    List<MedicionCalidadLacteaEntity> findByOrdenProduccionIdOrderByFechaHoraMedicionDesc(Long idOrdenProduccion);

    boolean existsByOrdenProduccionIdAndEjecucionBatchIdAndTipoMedicion(
            Long idOrdenProduccion,
            Long idEjecucionBatch,
            TipoMedicionCalidadLactea tipoMedicion);
}