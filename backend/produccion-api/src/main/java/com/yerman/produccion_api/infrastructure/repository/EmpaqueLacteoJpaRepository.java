package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.EmpaqueLacteoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpaqueLacteoJpaRepository extends JpaRepository<EmpaqueLacteoEntity, Long> {

    List<EmpaqueLacteoEntity> findByProductoTerminadoLacteo_Id(Long productoTerminadoLacteoId);

    List<EmpaqueLacteoEntity> findByLoteEmpaque(String loteEmpaque);

    boolean existsByLoteEmpaque(String loteEmpaque);
}