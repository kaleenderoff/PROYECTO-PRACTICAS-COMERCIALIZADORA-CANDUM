package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoTerminadoJpaRepository extends JpaRepository<ProductoTerminadoEntity, Long> {

    Optional<ProductoTerminadoEntity> findBySku(String sku);

    List<ProductoTerminadoEntity> findByActivoTrue();

    boolean existsBySku(String sku);
}
