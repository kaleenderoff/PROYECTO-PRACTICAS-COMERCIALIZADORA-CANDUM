package com.yerman.produccion_api.domain.model;

import java.time.LocalDateTime;

public class Validacion {

    private Long idValidacion;
    private Long idDetalleProduccion;
    private Long idValidador;
    private String estado;
    private String observacion;
    private LocalDateTime fechaValidacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Validacion() {
    }

    public Validacion(Long idValidacion, Long idDetalleProduccion, Long idValidador, String estado,
            String observacion, LocalDateTime fechaValidacion,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idValidacion = idValidacion;
        this.idDetalleProduccion = idDetalleProduccion;
        this.idValidador = idValidador;
        this.estado = estado;
        this.observacion = observacion;
        this.fechaValidacion = fechaValidacion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getIdValidacion() {
        return idValidacion;
    }

    public void setIdValidacion(Long idValidacion) {
        this.idValidacion = idValidacion;
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public Long getIdValidador() {
        return idValidador;
    }

    public void setIdValidador(Long idValidador) {
        this.idValidador = idValidador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
