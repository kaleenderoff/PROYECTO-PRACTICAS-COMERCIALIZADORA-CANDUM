package com.yerman.produccion_api.application.dto.request;

import java.time.LocalDate;

public class ProduccionFiltroRequest {

    private String numeroLote;
    private String estado;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Long idLineaProduccion;
    private Long idOperario;
    private Long idJefeLinea;

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

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
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
}