package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ConsumoInsumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumoInsumoJpaRepository extends JpaRepository<ConsumoInsumoEntity, Long> {

    List<ConsumoInsumoEntity> findByProduccion_IdProduccion(Long idProduccion);

    List<ConsumoInsumoEntity> findByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    List<ConsumoInsumoEntity> findByInsumo_IdInsumo(Long idInsumo);

    Optional<ConsumoInsumoEntity> findByProduccion_IdProduccionAndInsumo_IdInsumoAndDetalleProduccion_IdDetalleProduccion(
            Long idProduccion,
            Long idInsumo,
            Long idDetalleProduccion);

    boolean existsByProduccion_IdProduccionAndInsumo_IdInsumoAndDetalleProduccion_IdDetalleProduccion(
            Long idProduccion,
            Long idInsumo,
            Long idDetalleProduccion);
}
