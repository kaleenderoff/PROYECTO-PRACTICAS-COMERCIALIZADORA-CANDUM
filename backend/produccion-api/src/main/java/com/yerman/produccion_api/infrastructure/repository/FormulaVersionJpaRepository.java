package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormulaVersionJpaRepository extends JpaRepository<FormulaVersionEntity, Long> {
}