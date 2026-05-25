package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ValidacionOrdenProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidacionOrdenProduccionJpaRepository extends JpaRepository<ValidacionOrdenProduccionEntity, Long> {

    Optional<ValidacionOrdenProduccionEntity> findByOrdenId(Long idOrden);

    boolean existsByOrdenId(Long idOrden);

    boolean existsByOrdenIdAndAprobadoTrue(Long idOrden);
}
