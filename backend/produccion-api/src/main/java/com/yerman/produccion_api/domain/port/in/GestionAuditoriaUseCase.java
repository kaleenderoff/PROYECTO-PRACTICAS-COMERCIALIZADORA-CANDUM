package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.LogAuditoria;

import java.util.List;

public interface GestionAuditoriaUseCase {

    LogAuditoria registrar(Long idUsuario, String accion, String entidadAfectada,
            Long idRegistroAfectado, String detalle);

    List<LogAuditoria> listarTodos();

    List<LogAuditoria> listarPorUsuario(Long idUsuario);

    List<LogAuditoria> listarPorEntidad(String entidadAfectada);
}