package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

        @Query("""
                        SELECT p
                        FROM ProduccionEntity p
                        WHERE (:numeroLote IS NULL OR UPPER(p.numeroLote) LIKE UPPER(CONCAT('%', :numeroLote, '%')))
                          AND (:estado IS NULL OR UPPER(p.estado) = UPPER(:estado))
                          AND (:fechaDesde IS NULL OR p.fechaProduccion >= :fechaDesde)
                          AND (:fechaHasta IS NULL OR p.fechaProduccion <= :fechaHasta)
                          AND (:idLineaProduccion IS NULL OR p.lineaProduccion.idLineaProduccion = :idLineaProduccion)
                          AND (:idOperario IS NULL OR p.operario.idUsuario = :idOperario)
                          AND (:idJefeLinea IS NULL OR p.jefeLinea.idUsuario = :idJefeLinea)
                        """)
        Page<ProduccionEntity> filtrar(
                        @Param("numeroLote") String numeroLote,
                        @Param("estado") String estado,
                        @Param("fechaDesde") LocalDate fechaDesde,
                        @Param("fechaHasta") LocalDate fechaHasta,
                        @Param("idLineaProduccion") Long idLineaProduccion,
                        @Param("idOperario") Long idOperario,
                        @Param("idJefeLinea") Long idJefeLinea,
                        Pageable pageable);
}