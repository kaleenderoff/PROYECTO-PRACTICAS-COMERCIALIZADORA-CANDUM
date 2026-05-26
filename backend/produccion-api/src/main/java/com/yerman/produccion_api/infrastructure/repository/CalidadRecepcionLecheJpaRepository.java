package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.CalidadRecepcionLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalidadRecepcionLecheJpaRepository extends JpaRepository<CalidadRecepcionLecheEntity, Long> {

    List<CalidadRecepcionLecheEntity> findByRecepcionLecheIdOrderByFechaControlDescIdDesc(Long idRecepcionLeche);
}
