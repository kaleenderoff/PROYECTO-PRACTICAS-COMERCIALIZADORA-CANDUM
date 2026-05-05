package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduccionLacteaBatchJpaRepository extends JpaRepository<ProduccionLacteaBatchEntity, Long> {

    List<ProduccionLacteaBatchEntity> findByProduccionId(Long idProduccion);

    boolean existsByIdAndProduccionId(Long id, Long idProduccion);
}
