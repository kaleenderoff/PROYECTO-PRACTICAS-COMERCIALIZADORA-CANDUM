package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
