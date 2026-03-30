package com.yerman.produccion_api.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Produccion {

    private Long idProduccion;
    private LocalDate fechaProduccion;
    private String tipoTurno;
    private String numeroLote;
    private LocalDate fechaVencimiento;
    private String estado;
    private String observacionesGenerales;
    private Long idOperario;
    private Long idJefeLinea;
    private LineaProduccion lineaProduccion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Produccion() {
    }

    public Produccion(Long idProduccion, LocalDate fechaProduccion, String tipoTurno, String numeroLote,
            LocalDate fechaVencimiento, String estado, String observacionesGenerales,
            Long idOperario, Long idJefeLinea, LineaProduccion lineaProduccion,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idProduccion = idProduccion;
        this.fechaProduccion = fechaProduccion;
        this.tipoTurno = tipoTurno;
        this.numeroLote = numeroLote;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.observacionesGenerales = observacionesGenerales;
        this.idOperario = idOperario;
        this.idJefeLinea = idJefeLinea;
        this.lineaProduccion = lineaProduccion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public LineaProduccion getLineaProduccion() {
        return lineaProduccion;
    }

    public void setLineaProduccion(LineaProduccion lineaProduccion) {
        this.lineaProduccion = lineaProduccion;
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
