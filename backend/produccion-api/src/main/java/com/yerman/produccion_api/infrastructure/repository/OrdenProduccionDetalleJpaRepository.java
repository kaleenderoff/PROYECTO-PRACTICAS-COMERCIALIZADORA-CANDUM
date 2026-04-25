package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenProduccionDetalleJpaRepository extends JpaRepository<OrdenProduccionDetalleEntity, Long> {

    List<OrdenProduccionDetalleEntity> findByOrdenId(Long idOrden);

    boolean existsByOrdenIdAndSkuId(Long idOrden, Long idSku);
}