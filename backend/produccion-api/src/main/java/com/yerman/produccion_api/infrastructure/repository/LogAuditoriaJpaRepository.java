package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.LogAuditoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAuditoriaJpaRepository extends JpaRepository<LogAuditoriaEntity, Long> {

    Page<LogAuditoriaEntity> findAllByOrderByFechaHoraDesc(Pageable pageable);
}
