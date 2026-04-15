package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProduccionResponse {

    private Long idProduccion;
    private LocalDate fechaProduccion;
    private String tipoTurno;
    private String numeroLote;
    private LocalDate fechaVencimiento;
    private String estado;
    private String observacionesGenerales;

    private Long idLineaProduccion;
    private String nombreLineaProduccion;

    private Long idOperario;
    private String nombreOperario;

    private Long idJefeLinea;
    private String nombreJefeLinea;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public Long getIdLineaProduccion() {
        return idLineaProduccion;
    }

    public void setIdLineaProduccion(Long idLineaProduccion) {
        this.idLineaProduccion = idLineaProduccion;
    }

    public String getNombreLineaProduccion() {
        return nombreLineaProduccion;
    }

    public void setNombreLineaProduccion(String nombreLineaProduccion) {
        this.nombreLineaProduccion = nombreLineaProduccion;
    }

    public Long getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(Long idOperario) {
        this.idOperario = idOperario;
    }

    public String getNombreOperario() {
        return nombreOperario;
    }

    public void setNombreOperario(String nombreOperario) {
        this.nombreOperario = nombreOperario;
    }

    public Long getIdJefeLinea() {
        return idJefeLinea;
    }

    public void setIdJefeLinea(Long idJefeLinea) {
        this.idJefeLinea = idJefeLinea;
    }

    public String getNombreJefeLinea() {
        return nombreJefeLinea;
    }

    public void setNombreJefeLinea(String nombreJefeLinea) {
        this.nombreJefeLinea = nombreJefeLinea;
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