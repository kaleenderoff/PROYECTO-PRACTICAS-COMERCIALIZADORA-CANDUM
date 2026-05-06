package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TanqueLecheJpaRepository extends JpaRepository<TanqueLecheEntity, Long> {

    List<TanqueLecheEntity> findAllByOrderByIdAsc();
}
