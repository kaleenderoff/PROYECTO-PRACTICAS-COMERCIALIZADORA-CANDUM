package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdenProduccionJpaRepository extends JpaRepository<OrdenProduccionEntity, Long> {

    List<OrdenProduccionEntity> findByFechaProduccion(LocalDate fechaProduccion);

    boolean existsByProgramacionId(Long idProgramacion);

    Optional<OrdenProduccionEntity> findByNumeroOrden(String numeroOrden);
}