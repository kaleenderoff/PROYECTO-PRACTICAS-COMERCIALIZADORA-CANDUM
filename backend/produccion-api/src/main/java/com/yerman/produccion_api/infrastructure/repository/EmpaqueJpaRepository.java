package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.EmpaqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmpaqueJpaRepository extends JpaRepository<EmpaqueEntity, Long> {

    List<EmpaqueEntity> findByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    List<EmpaqueEntity> findByProductoTerminado_Id(Long idProductoTerminado);

    List<EmpaqueEntity> findByFechaEmpaqueBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<EmpaqueEntity> findByLoteEmpaque(String loteEmpaque);
}
