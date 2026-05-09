package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.FormulaDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormulaDetalleJpaRepository extends JpaRepository<FormulaDetalleEntity, Long> {

    List<FormulaDetalleEntity> findByFormulaVersionIdOrderByOrdenAdicionAsc(Long idFormulaVersion);

    Optional<FormulaDetalleEntity> findByFormulaVersionIdAndInsumoId(Long idFormulaVersion, Long idInsumo);

    boolean existsByFormulaVersionIdAndInsumoId(Long idFormulaVersion, Long idInsumo);
}