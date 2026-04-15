package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.infrastructure.entity.LogAuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogAuditoriaJpaRepository extends JpaRepository<LogAuditoriaEntity, Long> {

    List<LogAuditoriaEntity> findByUsuario_IdUsuarioOrderByFechaHoraDesc(Long idUsuario);

    List<LogAuditoriaEntity> findByEntidadAfectadaOrderByFechaHoraDesc(EntidadAuditoria entidadAfectada);

    List<LogAuditoriaEntity> findAllByOrderByFechaHoraDesc();
}