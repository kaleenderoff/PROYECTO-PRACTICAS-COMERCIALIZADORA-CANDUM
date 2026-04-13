package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public class DashboardTrazabilidadLoteResponse {

    private Long idProduccion;
    private String numeroLote;
    private LocalDate fechaProduccion;
    private LocalDate fechaVencimiento;
    private String estadoProduccion;
    private String tipoTurno;
    private String lineaProduccion;
    private List<DashboardTrazabilidadDetalleResponse> detalles;

    public DashboardTrazabilidadLoteResponse() {
    }

    public Long getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Long idProduccion) {
        this.idProduccion = idProduccion;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstadoProduccion() {
        return estadoProduccion;
    }

    public void setEstadoProduccion(String estadoProduccion) {
        this.estadoProduccion = estadoProduccion;
    }

    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public String getLineaProduccion() {
        return lineaProduccion;
    }

    public void setLineaProduccion(String lineaProduccion) {
        this.lineaProduccion = lineaProduccion;
    }

    public List<DashboardTrazabilidadDetalleResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DashboardTrazabilidadDetalleResponse> detalles) {
        this.detalles = detalles;
    }
}