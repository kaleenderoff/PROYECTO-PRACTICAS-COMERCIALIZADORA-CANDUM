package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DetalleProduccion {

    private Long idDetalleProduccion;
    private Long idProduccion;
    private Producto producto;
    private BigDecimal kgProgramados;
    private BigDecimal kgBatch;
    private Integer numBatch;
    private Integer unidadesReales;
    private BigDecimal rendimientoPct;
    private String observaciones;
    private LocalDateTime fechaHoraRegistro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DetalleProduccion() {
    }

    public DetalleProduccion(Long idDetalleProduccion, Long idProduccion, Producto producto,
            BigDecimal kgProgramados, BigDecimal kgBatch, Integer numBatch,
            Integer unidadesReales, BigDecimal rendimientoPct, String observaciones,
            LocalDateTime fechaHoraRegistro, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idDetalleProduccion = idDetalleProduccion;
        this.idProduccion = idProduccion;
        this.producto = producto;
        this.kgProgramados = kgProgramados;
        this.kgBatch = kgBatch;
        this.numBatch = numBatch;
        this.unidadesReales = unidadesReales;
        this.rendimientoPct = rendimientoPct;
        this.observaciones = observaciones;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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
