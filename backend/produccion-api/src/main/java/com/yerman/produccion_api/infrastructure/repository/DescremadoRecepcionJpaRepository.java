package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescremadoRecepcionJpaRepository extends JpaRepository<DescremadoRecepcionEntity, Long> {

    List<DescremadoRecepcionEntity> findByRecepcionLecheIdOrderByIdDesc(Long idRecepcionLeche);
}