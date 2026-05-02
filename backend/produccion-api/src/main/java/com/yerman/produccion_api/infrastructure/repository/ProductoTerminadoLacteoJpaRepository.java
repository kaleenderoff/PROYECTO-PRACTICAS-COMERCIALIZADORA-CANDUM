package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoTerminadoLacteoJpaRepository extends JpaRepository<ProductoTerminadoLacteoEntity, Long> {

    List<ProductoTerminadoLacteoEntity> findByEstadoOrderByIdDesc(EstadoProductoTerminadoLacteo estado);

    List<ProductoTerminadoLacteoEntity> findByProductoContainingIgnoreCaseOrderByIdDesc(String producto);
}