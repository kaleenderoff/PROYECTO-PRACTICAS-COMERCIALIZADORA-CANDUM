package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {

    Optional<ProductoEntity> findByNombreAndGramajeGAndMarca(String nombre, BigDecimal gramajeG, String marca);

    boolean existsByNombreAndGramajeGAndMarca(String nombre, BigDecimal gramajeG, String marca);

    List<ProductoEntity> findByActivoTrue();

    List<ProductoEntity> findByLineaProduccion_IdLineaProduccion(Long idLineaProduccion);

    List<ProductoEntity> findByLineaProduccion_IdLineaProduccionAndActivoTrue(Long idLineaProduccion);
}