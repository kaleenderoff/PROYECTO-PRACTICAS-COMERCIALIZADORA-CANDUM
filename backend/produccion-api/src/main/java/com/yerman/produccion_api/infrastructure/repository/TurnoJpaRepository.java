package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.TurnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurnoJpaRepository extends JpaRepository<TurnoEntity, Long> {
    boolean existsByNombre(String nombre);

    List<TurnoEntity> findByActivoTrue();
}
