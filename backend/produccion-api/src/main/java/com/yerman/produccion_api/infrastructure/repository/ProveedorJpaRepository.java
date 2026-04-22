package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorJpaRepository extends JpaRepository<ProveedorEntity, Long> {
    boolean existsByNombre(String nombre);

    List<ProveedorEntity> findByActivoTrue();
}
