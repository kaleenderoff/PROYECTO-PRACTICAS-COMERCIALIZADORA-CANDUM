package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.infrastructure.entity.ValidacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ValidacionJpaRepository extends JpaRepository<ValidacionEntity, Long> {

    Optional<ValidacionEntity> findByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    boolean existsByDetalleProduccion_IdDetalleProduccion(Long idDetalleProduccion);

    List<ValidacionEntity> findByEstado(EstadoValidacion estado);

    List<ValidacionEntity> findByValidador_IdUsuario(Long idValidador);

    @Query("""
            select v
            from ValidacionEntity v
            join fetch v.detalleProduccion d
            join fetch d.produccion p
            join fetch v.validador val
            order by v.fechaValidacion desc
            """)
    List<ValidacionEntity> findAllConRelaciones();
}