package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardValidacionPendienteResponse {

    private Long idDetalleProduccion;
    private Long idProduccion;
    private String numeroLote;
    private LocalDate fechaProduccion;
    private String estadoProduccion;
    private Long idProducto;
    private String nombreProducto;
    private Integer numBatch;
    private BigDecimal kgProgramados;
    private BigDecimal kgBatch;
    private Integer unidadesReales;
    private BigDecimal rendimientoPct;

    public DashboardValidacionPendienteResponse() {
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
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

    public String getEstadoProduccion() {
        return estadoProduccion;
    }

    public void setEstadoProduccion(String estadoProduccion) {
        this.estadoProduccion = estadoProduccion;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getNumBatch() {
        return numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public BigDecimal getKgProgramados() {
        return kgProgramados;
    }

    public void setKgProgramados(BigDecimal kgProgramados) {
        this.kgProgramados = kgProgramados;
    }

    public BigDecimal getKgBatch() {
        return kgBatch;
    }

    public void setKgBatch(BigDecimal kgBatch) {
        this.kgBatch = kgBatch;
    }

    public Integer getUnidadesReales() {
        return unidadesReales;
    }

    public void setUnidadesReales(Integer unidadesReales) {
        this.unidadesReales = unidadesReales;
    }

    public BigDecimal getRendimientoPct() {
        return rendimientoPct;
    }

    public void setRendimientoPct(BigDecimal rendimientoPct) {
        this.rendimientoPct = rendimientoPct;
    }
}