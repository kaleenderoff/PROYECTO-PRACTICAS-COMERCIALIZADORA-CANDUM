package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.InsumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsumoJpaRepository extends JpaRepository<InsumoEntity, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByCodigo(String codigo);

    List<InsumoEntity> findByActivoTrue();
}
