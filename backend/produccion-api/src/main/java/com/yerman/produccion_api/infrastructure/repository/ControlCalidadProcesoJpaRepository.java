package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ControlCalidadProcesoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ControlCalidadProcesoJpaRepository extends JpaRepository<ControlCalidadProcesoEntity, Long> {

    List<ControlCalidadProcesoEntity> findByOrdenProduccionIdOrderByFechaProduccionDescIdDesc(Long idOrdenProduccion);
}
