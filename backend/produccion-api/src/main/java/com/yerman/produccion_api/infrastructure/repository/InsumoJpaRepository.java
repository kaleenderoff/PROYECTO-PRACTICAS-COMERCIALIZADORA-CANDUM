package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.InsumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoJpaRepository extends JpaRepository<InsumoEntity, Long> {
}
