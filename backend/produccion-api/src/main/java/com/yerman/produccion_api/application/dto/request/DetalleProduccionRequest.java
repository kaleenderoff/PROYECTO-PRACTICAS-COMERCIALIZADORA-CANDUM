package com.yerman.produccion_api.application.dto.request;

import java.math.BigDecimal;

public class DetalleProduccionRequest {

    private Long idProduccion;
    private Long idProducto;
    private BigDecimal kgProgramados;
    private BigDecimal kgBatch;
    private Integer numBatch;
    private Integer unidadesReales;
    private String observaciones;

    public Long getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Long idProduccion) {
        this.idProduccion = idProduccion;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}