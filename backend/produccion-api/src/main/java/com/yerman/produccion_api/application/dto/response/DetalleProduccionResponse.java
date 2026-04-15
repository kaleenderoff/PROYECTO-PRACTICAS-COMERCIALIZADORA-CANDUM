package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DetalleProduccionResponse {

    private Long idDetalleProduccion;
    private Long idProduccion;
    private String numeroLoteProduccion;
    private LocalDate fechaProduccion;
    private String estadoProduccion;

    private Long idProducto;
    private String nombreProducto;

    private BigDecimal kgProgramados;
    private BigDecimal kgBatch;
    private Integer numBatch;
    private Integer unidadesReales;
    private BigDecimal rendimientoPct;
    private String observaciones;
    private LocalDateTime fechaHoraRegistro;

    private Boolean tieneValidacion;

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

    public String getNumeroLoteProduccion() {
        return numeroLoteProduccion;
    }

    public void setNumeroLoteProduccion(String numeroLoteProduccion) {
        this.numeroLoteProduccion = numeroLoteProduccion;
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

    public Integer getNumBatch() {
        return numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Boolean getTieneValidacion() {
        return tieneValidacion;
    }

    public void setTieneValidacion(Boolean tieneValidacion) {
        this.tieneValidacion = tieneValidacion;
    }
}