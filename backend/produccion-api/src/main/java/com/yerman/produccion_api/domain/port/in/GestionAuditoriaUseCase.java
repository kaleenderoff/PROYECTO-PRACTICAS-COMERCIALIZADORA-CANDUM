package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.AccionAuditoria;
import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.domain.model.LogAuditoria;

import java.util.List;

public interface GestionAuditoriaUseCase {

    LogAuditoria registrar(Long idUsuario, AccionAuditoria accion, EntidadAuditoria entidadAfectada,
            Long idRegistroAfectado, String detalle);

    List<LogAuditoria> listarTodos();

    List<LogAuditoria> listarPorUsuario(Long idUsuario);

    List<LogAuditoria> listarPorEntidad(EntidadAuditoria entidadAfectada);
}