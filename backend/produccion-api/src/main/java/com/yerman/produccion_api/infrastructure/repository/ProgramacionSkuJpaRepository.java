package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramacionSkuJpaRepository extends JpaRepository<ProgramacionSkuEntity, Long> {

    List<ProgramacionSkuEntity> findByProgramacionIdOrderByIdAsc(Long idProgramacion);

    boolean existsByProgramacionIdAndSkuId(Long idProgramacion, Long idSku);
}