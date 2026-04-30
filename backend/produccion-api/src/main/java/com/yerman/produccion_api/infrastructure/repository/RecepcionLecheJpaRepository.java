package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecepcionLecheJpaRepository extends JpaRepository<RecepcionLecheEntity, Long> {

    List<RecepcionLecheEntity> findByFechaRecepcionOrderByIdDesc(LocalDate fechaRecepcion);

    List<RecepcionLecheEntity> findByProveedorContainingIgnoreCaseOrderByIdDesc(String proveedor);
}