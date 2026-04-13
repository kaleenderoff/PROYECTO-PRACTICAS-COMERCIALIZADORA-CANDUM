package com.yerman.produccion_api.domain.model;

import java.time.LocalDateTime;

public class LogAuditoria {

    private Long idLog;
    private Long idUsuario;
    private String accion;
    private String entidadAfectada;
    private Long idRegistroAfectado;
    private String detalle;
    private LocalDateTime fechaHora;

    public LogAuditoria() {
    }

    public LogAuditoria(Long idLog, Long idUsuario, String accion, String entidadAfectada,
            Long idRegistroAfectado, String detalle, LocalDateTime fechaHora) {
        this.idLog = idLog;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.entidadAfectada = entidadAfectada;
        this.idRegistroAfectado = idRegistroAfectado;
        this.detalle = detalle;
        this.fechaHora = fechaHora;
    }

    public Long getIdLog() {
        return idLog;
    }

    public void setIdLog(Long idLog) {
        this.idLog = idLog;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidadAfectada() {
        return entidadAfectada;
    }

    public void setEntidadAfectada(String entidadAfectada) {
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
