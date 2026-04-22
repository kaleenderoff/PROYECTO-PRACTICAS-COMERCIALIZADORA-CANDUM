package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.CatalogoLineaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogoLineaJpaRepository extends JpaRepository<CatalogoLineaEntity, Long> {
    boolean existsByNombre(String nombre);

    List<CatalogoLineaEntity> findByActivoTrue();
}
