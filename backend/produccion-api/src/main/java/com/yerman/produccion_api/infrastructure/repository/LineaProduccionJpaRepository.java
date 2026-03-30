package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.LineaProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LineaProduccionJpaRepository extends JpaRepository<LineaProduccionEntity, Long> {

    Optional<LineaProduccionEntity> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<LineaProduccionEntity> findByActivoTrue();
}