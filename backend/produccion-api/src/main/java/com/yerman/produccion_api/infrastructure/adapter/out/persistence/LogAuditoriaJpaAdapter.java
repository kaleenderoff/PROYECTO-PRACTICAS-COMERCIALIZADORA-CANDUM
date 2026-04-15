package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.domain.model.LogAuditoria;
import com.yerman.produccion_api.domain.port.out.LogAuditoriaRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.LogAuditoriaEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.LogAuditoriaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogAuditoriaJpaAdapter implements LogAuditoriaRepositoryPort {

    private final LogAuditoriaJpaRepository repository;

    public LogAuditoriaJpaAdapter(LogAuditoriaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LogAuditoria guardar(LogAuditoria logAuditoria) {
        LogAuditoriaEntity entity = toEntity(logAuditoria);
        LogAuditoriaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<LogAuditoria> listarTodos() {
        return repository.findAllByOrderByFechaHoraDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<LogAuditoria> listarPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuarioOrderByFechaHoraDesc(idUsuario)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<LogAuditoria> listarPorEntidad(EntidadAuditoria entidadAfectada) {
        return repository.findByEntidadAfectadaOrderByFechaHoraDesc(entidadAfectada)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private LogAuditoriaEntity toEntity(LogAuditoria log) {
        LogAuditoriaEntity entity = new LogAuditoriaEntity();
        entity.setIdLog(log.getIdLog());

        if (log.getIdUsuario() != null) {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setIdUsuario(log.getIdUsuario());
            entity.setUsuario(usuario);
        }

        entity.setAccion(log.getAccion());
        entity.setEntidadAfectada(log.getEntidadAfectada());
        entity.setIdRegistroAfectado(log.getIdRegistroAfectado());
        entity.setDetalle(log.getDetalle());
        entity.setFechaHora(log.getFechaHora());

        return entity;
    }

    private LogAuditoria toDomain(LogAuditoriaEntity entity) {
        LogAuditoria log = new LogAuditoria();
        log.setIdLog(entity.getIdLog());

        if (entity.getUsuario() != null) {
            log.setIdUsuario(entity.getUsuario().getIdUsuario());
        }

        log.setAccion(entity.getAccion());
        log.setEntidadAfectada(entity.getEntidadAfectada());
        log.setIdRegistroAfectado(entity.getIdRegistroAfectado());
        log.setDetalle(entity.getDetalle());
        log.setFechaHora(entity.getFechaHora());

        return log;
    }
}