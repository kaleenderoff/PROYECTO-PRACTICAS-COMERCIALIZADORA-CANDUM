package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProduccionLacteaJpaRepository extends JpaRepository<ProduccionLacteaEntity, Long> {

    List<ProduccionLacteaEntity> findByFechaProduccion(LocalDate fechaProduccion);
}