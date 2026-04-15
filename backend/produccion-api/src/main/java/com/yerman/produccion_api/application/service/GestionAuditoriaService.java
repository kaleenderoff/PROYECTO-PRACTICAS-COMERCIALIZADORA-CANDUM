package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.AccionAuditoria;
import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.domain.model.LogAuditoria;
import com.yerman.produccion_api.domain.port.in.GestionAuditoriaUseCase;
import com.yerman.produccion_api.domain.port.out.LogAuditoriaRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionAuditoriaService implements GestionAuditoriaUseCase {

    private final LogAuditoriaRepositoryPort logAuditoriaRepositoryPort;

    public GestionAuditoriaService(LogAuditoriaRepositoryPort logAuditoriaRepositoryPort) {
        this.logAuditoriaRepositoryPort = logAuditoriaRepositoryPort;
    }

    @Override
    public LogAuditoria registrar(Long idUsuario, AccionAuditoria accion, EntidadAuditoria entidadAfectada,
            Long idRegistroAfectado, String detalle) {

        if (idUsuario == null) {
            throw new ReglaNegocioException("El id del usuario es obligatorio para auditoría");
        }

        if (accion == null) {
            throw new ReglaNegocioException("La acción de auditoría es obligatoria");
        }

        if (entidadAfectada == null) {
            throw new ReglaNegocioException("La entidad afectada es obligatoria");
        }

        LogAuditoria log = new LogAuditoria();
        log.setIdUsuario(idUsuario);
        log.setAccion(accion);
        log.setEntidadAfectada(entidadAfectada);
        log.setIdRegistroAfectado(idRegistroAfectado);
        log.setDetalle(detalle != null && !detalle.trim().isEmpty() ? detalle.trim() : null);
        log.setFechaHora(LocalDateTime.now());

        return logAuditoriaRepositoryPort.guardar(log);
    }

    @Override
    public List<LogAuditoria> listarTodos() {
        return logAuditoriaRepositoryPort.listarTodos();
    }

    @Override
    public List<LogAuditoria> listarPorUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new ReglaNegocioException("El id del usuario es obligatorio");
        }
        return logAuditoriaRepositoryPort.listarPorUsuario(idUsuario);
    }

    @Override
    public List<LogAuditoria> listarPorEntidad(EntidadAuditoria entidadAfectada) {
        if (entidadAfectada == null) {
            throw new ReglaNegocioException("La entidad afectada es obligatoria");
        }
        return logAuditoriaRepositoryPort.listarPorEntidad(entidadAfectada);
    }
}