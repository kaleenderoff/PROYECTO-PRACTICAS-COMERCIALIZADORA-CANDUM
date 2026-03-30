package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProduccionJpaRepository extends JpaRepository<ProduccionEntity, Long> {

    Optional<ProduccionEntity> findByNumeroLote(String numeroLote);

    boolean existsByNumeroLote(String numeroLote);

    List<ProduccionEntity> findByFechaProduccion(LocalDate fechaProduccion);

    List<ProduccionEntity> findByEstado(String estado);

    List<ProduccionEntity> findByLineaProduccion_IdLineaProduccion(Long idLineaProduccion);

    List<ProduccionEntity> findByOperario_IdUsuario(Long idOperario);

    List<ProduccionEntity> findByJefeLinea_IdUsuario(Long idJefeLinea);
}
