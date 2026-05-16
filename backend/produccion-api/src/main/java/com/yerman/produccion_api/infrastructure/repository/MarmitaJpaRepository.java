package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.MarmitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarmitaJpaRepository extends JpaRepository<MarmitaEntity, Long> {
    List<MarmitaEntity> findByActivaTrue();
}
