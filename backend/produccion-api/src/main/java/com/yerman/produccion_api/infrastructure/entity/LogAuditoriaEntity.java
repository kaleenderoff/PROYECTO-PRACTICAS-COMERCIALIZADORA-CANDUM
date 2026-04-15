package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.AccionAuditoria;
import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
public class LogAuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false, length = 50)
    private AccionAuditoria accion;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidad_afectada", nullable = false, length = 50)
    private EntidadAuditoria entidadAfectada;

    @Column(name = "id_registro_afectado")
    private Long idRegistroAfectado;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @PrePersist
    protected void onCreate() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
    }

    public Long getIdLog() {
        return idLog;
    }

    public void setIdLog(Long idLog) {
        this.idLog = idLog;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public AccionAuditoria getAccion() {
        return accion;
    }

    public void setAccion(AccionAuditoria accion) {
        this.accion = accion;
    }

    public EntidadAuditoria getEntidadAfectada() {
        return entidadAfectada;
    }

    public void setEntidadAfectada(EntidadAuditoria entidadAfectada) {
        this.entidadAfectada = entidadAfectada;
    }

    public Long getIdRegistroAfectado() {
        return idRegistroAfectado;
    }

    public void setIdRegistroAfectado(Long idRegistroAfectado) {
        this.idRegistroAfectado = idRegistroAfectado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}