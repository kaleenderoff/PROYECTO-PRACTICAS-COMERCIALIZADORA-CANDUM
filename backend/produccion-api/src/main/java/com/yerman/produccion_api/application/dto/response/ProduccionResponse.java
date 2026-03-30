package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProduccionResponse {

    private Long idProduccion;
    private LocalDate fechaProduccion;
    private String tipoTurno;
    private String numeroLote;
    private String estado;

    private Long idLineaProduccion;
    private Long idOperario;
    private Long idJefeLinea;

    private LocalDateTime createdAt;

    public Long getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Long idProduccion) {
        this.idProduccion = idProduccion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdLineaProduccion() {
        return idLineaProduccion;
    }

    public void setIdLineaProduccion(Long idLineaProduccion) {
        this.idLineaProduccion = idLineaProduccion;
    }

    public Long getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(Long idOperario) {
        this.idOperario = idOperario;
    }

    public Long getIdJefeLinea() {
        return idJefeLinea;
    }

    public void setIdJefeLinea(Long idJefeLinea) {
        this.idJefeLinea = idJefeLinea;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
