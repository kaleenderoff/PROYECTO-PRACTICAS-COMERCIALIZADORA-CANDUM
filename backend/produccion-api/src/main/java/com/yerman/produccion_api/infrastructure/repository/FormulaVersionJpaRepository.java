package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormulaVersionJpaRepository extends JpaRepository<FormulaVersionEntity, Long> {

    Optional<FormulaVersionEntity> findFirstByFormulaProductoIdAndEstadoOrderByFechaInicioVigenciaDesc(
            Long idProducto,
            FormulaVersionEntity.EstadoFormula estado);

    List<FormulaVersionEntity> findByFormulaIdOrderByFechaInicioVigenciaDesc(Long idFormula);

    List<FormulaVersionEntity> findByFormulaIdAndEstado(
            Long idFormula,
            FormulaVersionEntity.EstadoFormula estado);

    boolean existsByFormulaIdAndVersionIgnoreCase(Long idFormula, String version);
}