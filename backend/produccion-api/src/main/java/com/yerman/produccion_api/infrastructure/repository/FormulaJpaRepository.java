package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.FormulaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormulaJpaRepository extends JpaRepository<FormulaEntity, Long> {

    List<FormulaEntity> findByProductoIdAndActivoTrue(Long idProducto);

    Optional<FormulaEntity> findByProductoIdAndNombreIgnoreCase(Long idProducto, String nombre);

    boolean existsByProductoIdAndNombreIgnoreCase(Long idProducto, String nombre);
}