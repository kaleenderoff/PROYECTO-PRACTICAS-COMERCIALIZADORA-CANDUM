package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ControlPesoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ControlPesoProductoJpaRepository extends JpaRepository<ControlPesoProductoEntity, Long> {

    List<ControlPesoProductoEntity> findByOrdenProduccionIdOrderByFechaControlDescIdDesc(Long idOrdenProduccion);
}
