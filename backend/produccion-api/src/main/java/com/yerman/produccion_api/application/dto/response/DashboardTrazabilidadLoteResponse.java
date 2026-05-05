package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardTrazabilidadLoteResponse {

    private String lote;
    private String producto;
    private LocalDate fechaProduccion;
    private Integer numeroBatch;
    private BigDecimal kilosProducidos;
    private BigDecimal kilosDisponibles;
    private BigDecimal kilosEmpacados;
    private String estadoProductoTerminado;

    public DashboardTrazabilidadLoteResponse() {
    }

    public DashboardTrazabilidadLoteResponse(
            String lote,
            String producto,
            LocalDate fechaProduccion,
            Integer numeroBatch,
            BigDecimal kilosProducidos,
            BigDecimal kilosDisponibles,
            BigDecimal kilosEmpacados,
            String estadoProductoTerminado) {
        this.lote = lote;
        this.producto = producto;
        this.fechaProduccion = fechaProduccion;
        this.numeroBatch = numeroBatch;
        this.kilosProducidos = kilosProducidos;
        this.kilosDisponibles = kilosDisponibles;
        this.kilosEmpacados = kilosEmpacados;
        this.estadoProductoTerminado = estadoProductoTerminado;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public Integer getNumeroBatch() {
        return numeroBatch;
    }

    public void setNumeroBatch(Integer numeroBatch) {
        this.numeroBatch = numeroBatch;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public BigDecimal getKilosDisponibles() {
        return kilosDisponibles;
    }

    public void setKilosDisponibles(BigDecimal kilosDisponibles) {
        this.kilosDisponibles = kilosDisponibles;
    }

    public BigDecimal getKilosEmpacados() {
        return kilosEmpacados;
    }

    public void setKilosEmpacados(BigDecimal kilosEmpacados) {
        this.kilosEmpacados = kilosEmpacados;
    }

    public String getEstadoProductoTerminado() {
        return estadoProductoTerminado;
    }

    public void setEstadoProductoTerminado(String estadoProductoTerminado) {
        this.estadoProductoTerminado = estadoProductoTerminado;
    }
}