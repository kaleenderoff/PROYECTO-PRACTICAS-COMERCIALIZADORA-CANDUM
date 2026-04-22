package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatalogoSkuJpaRepository extends JpaRepository<CatalogoSkuEntity, Long> {
    boolean existsByCodigoSku(String codigoSku);

    Optional<CatalogoSkuEntity> findByCodigoSku(String codigoSku);

    List<CatalogoSkuEntity> findByActivoTrue();

    List<CatalogoSkuEntity> findByProductoId(Long idProducto);
}
