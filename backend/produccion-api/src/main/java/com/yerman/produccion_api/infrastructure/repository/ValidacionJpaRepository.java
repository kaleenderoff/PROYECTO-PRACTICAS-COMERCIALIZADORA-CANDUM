package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.infrastructure.entity.ValidacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ValidacionJpaRepository extends JpaRepository<ValidacionEntity, Long> {

    Optional<ValidacionEntity> findByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    boolean existsByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    List<ValidacionEntity> findByEstado(EstadoValidacion estado);

    List<ValidacionEntity> findByValidador_IdUsuario(Long idValidador);
}