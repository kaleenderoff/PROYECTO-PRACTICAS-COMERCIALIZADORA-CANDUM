package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDateTime;

public class LogAuditoriaResponse {

    private Long idLog;
    private Long idUsuario;
    private String accion;
    private String entidadAfectada;
    private Long idRegistroAfectado;
    private String detalle;
    private LocalDateTime fechaHora;

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