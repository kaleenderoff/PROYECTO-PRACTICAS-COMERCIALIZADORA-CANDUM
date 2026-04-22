package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcaJpaRepository extends JpaRepository<MarcaEntity, Long> {
    boolean existsByNombre(String nombre);

    List<MarcaEntity> findByActivoTrue();
}
