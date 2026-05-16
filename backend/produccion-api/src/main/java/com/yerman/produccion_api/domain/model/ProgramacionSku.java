package com.yerman.produccion_api.domain.model;

public class ProgramacionSku {

    private Long id;
    private Long idProgramacion;
    private Long idSku;
    private Integer unidadesObjetivo;
    private String observaciones;

    private String codigoSku;
    private String descripcionSku;
    private Integer pesoUnidadGr;
    private java.math.BigDecimal kgProductoTerminado;
    private java.math.BigDecimal rendimientoTeoricoPct;
    private java.math.BigDecimal kgBatchFormula;
    private java.math.BigDecimal kgBatchCalculado;
    private java.math.BigDecimal numBachesCalculado;
    private java.math.BigDecimal cantidadReal;
    private Integer unidadesReales;

    public ProgramacionSku() {
    }

    public ProgramacionSku(Long id, Long idProgramacion, Long idSku, Integer unidadesObjetivo, String observaciones) {
        this.id = id;
        this.idProgramacion = idProgramacion;
        this.idSku = idSku;
        this.unidadesObjetivo = unidadesObjetivo;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public void setIdProgramacion(Long idProgramacion) {
        this.idProgramacion = idProgramacion;
    }

    public Long getIdSku() {
        return idSku;
    }

    public void setIdSku(Long idSku) {
        this.idSku = idSku;
    }

    public String getCodigoSku() {
        return codigoSku;
    }

    public void setCodigoSku(String codigoSku) {
        this.codigoSku = codigoSku;
    }

    public String getDescripcionSku() {
        return descripcionSku;
    }

    public void setDescripcionSku(String descripcionSku) {
        this.descripcionSku = descripcionSku;
    }

    public Integer getPesoUnidadGr() {
        return pesoUnidadGr;
    }

    public void setPesoUnidadGr(Integer pesoUnidadGr) {
        this.pesoUnidadGr = pesoUnidadGr;
    }

    public Integer getUnidadesObjetivo() {
        return unidadesObjetivo;
    }

    public void setUnidadesObjetivo(Integer unidadesObjetivo) {
        this.unidadesObjetivo = unidadesObjetivo;
    }

    public java.math.BigDecimal getKgProductoTerminado() {
        return kgProductoTerminado;
    }

    public void setKgProductoTerminado(java.math.BigDecimal kgProductoTerminado) {
        this.kgProductoTerminado = kgProductoTerminado;
    }

    public java.math.BigDecimal getRendimientoTeoricoPct() {
        return rendimientoTeoricoPct;
    }

    public void setRendimientoTeoricoPct(java.math.BigDecimal rendimientoTeoricoPct) {
        this.rendimientoTeoricoPct = rendimientoTeoricoPct;
    }

    public java.math.BigDecimal getKgBatchFormula() {
        return kgBatchFormula;
    }

    public void setKgBatchFormula(java.math.BigDecimal kgBatchFormula) {
        this.kgBatchFormula = kgBatchFormula;
    }

    public java.math.BigDecimal getKgBatchCalculado() {
        return kgBatchCalculado;
    }

    public void setKgBatchCalculado(java.math.BigDecimal kgBatchCalculado) {
        this.kgBatchCalculado = kgBatchCalculado;
    }

    public java.math.BigDecimal getNumBachesCalculado() {
        return numBachesCalculado;
    }

    public void setNumBachesCalculado(java.math.BigDecimal numBachesCalculado) {
        this.numBachesCalculado = numBachesCalculado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public java.math.BigDecimal getCantidadReal() {
        return cantidadReal;
    }

    public void setCantidadReal(java.math.BigDecimal cantidadReal) {
        this.cantidadReal = cantidadReal;
    }

    public Integer getUnidadesReales() {
        return unidadesReales;
    }

    public void setUnidadesReales(Integer unidadesReales) {
        this.unidadesReales = unidadesReales;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}