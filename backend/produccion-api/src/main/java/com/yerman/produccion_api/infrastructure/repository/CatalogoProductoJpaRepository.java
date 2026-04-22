package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.CatalogoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogoProductoJpaRepository extends JpaRepository<CatalogoProductoEntity, Long> {
    boolean existsByLineaIdAndNombre(Long idLinea, String nombre);

    List<CatalogoProductoEntity> findByActivoTrue();

    List<CatalogoProductoEntity> findByLineaId(Long idLinea);
}
