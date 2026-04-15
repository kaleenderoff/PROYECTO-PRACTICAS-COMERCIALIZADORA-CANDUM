package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.domain.model.LogAuditoria;

import java.util.List;

public interface LogAuditoriaRepositoryPort {

    LogAuditoria guardar(LogAuditoria logAuditoria);

    List<LogAuditoria> listarTodos();

    List<LogAuditoria> listarPorUsuario(Long idUsuario);

    List<LogAuditoria> listarPorEntidad(EntidadAuditoria entidadAfectada);
}