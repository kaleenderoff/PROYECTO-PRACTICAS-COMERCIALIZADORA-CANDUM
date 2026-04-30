package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.RecepcionLechePesajeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecepcionLechePesajeJpaRepository extends JpaRepository<RecepcionLechePesajeEntity, Long> {

    List<RecepcionLechePesajeEntity> findByRecepcionLecheIdOrderByNumeroPesajeAsc(Long idRecepcionLeche);
}