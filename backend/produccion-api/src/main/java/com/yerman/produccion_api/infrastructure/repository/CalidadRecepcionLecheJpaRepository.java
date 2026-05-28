package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.CalidadRecepcionLecheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalidadRecepcionLecheJpaRepository extends JpaRepository<CalidadRecepcionLecheEntity, Long> {

    List<CalidadRecepcionLecheEntity> findByRecepcionLecheIdOrderByFechaControlDescIdDesc(Long idRecepcionLeche);

    boolean existsByRecepcionLecheId(Long idRecepcionLeche);

    java.util.Optional<CalidadRecepcionLecheEntity> findFirstByRecepcionLecheIdOrderByFechaControlDescIdDesc(
            Long idRecepcionLeche);

    /**
     * Obtiene el estado de calidad más reciente por recepcion (APROBADA / RETENIDA).
     * Retorna Object[] { idRecepcionLeche (Long), aprobado (Boolean), retenido (Boolean) }
     */
    @Query("SELECT c.recepcionLeche.id, c.aprobado, c.retenido " +
           "FROM CalidadRecepcionLecheEntity c " +
           "WHERE c.id = (" +
           "  SELECT MAX(c2.id) FROM CalidadRecepcionLecheEntity c2 " +
           "  WHERE c2.recepcionLeche.id = c.recepcionLeche.id" +
           ")")
    List<Object[]> findLatestEstadoPorRecepcion();

    @Query("SELECT c FROM CalidadRecepcionLecheEntity c " +
           "ORDER BY c.recepcionLeche.id ASC, c.fechaControl DESC, c.id DESC")
    List<CalidadRecepcionLecheEntity> findAllOrdenadasParaEstadoRecepcion();
}
