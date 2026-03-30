package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.InsumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InsumoJpaRepository extends JpaRepository<InsumoEntity, Long> {

    Optional<InsumoEntity> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<InsumoEntity> findByActivoTrue();
}
