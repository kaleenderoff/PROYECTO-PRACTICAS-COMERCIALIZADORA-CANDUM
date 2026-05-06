package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.RegistroInsumoLacteoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RegistroInsumoLacteoJpaRepository extends JpaRepository<RegistroInsumoLacteoEntity, Long> {

    List<RegistroInsumoLacteoEntity> findByProduccionLacteaIdOrderByFechaHoraRegistroDesc(Long idProduccionLactea);

    List<RegistroInsumoLacteoEntity> findByProduccionLacteaBatchIdOrderByFechaHoraRegistroDesc(Long idProduccionLacteaBatch);

    List<RegistroInsumoLacteoEntity> findByProduccionLacteaFechaProduccionOrderByProduccionLacteaIdAscFechaHoraRegistroAsc(
            LocalDate fechaProduccion);
}
