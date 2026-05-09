package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class ProgramacionSkuResponse {

    private Long id;
    private Long idProgramacion;
    private Long idSku;
    private String codigoSku;
    private String descripcionSku;
    private Integer pesoUnidadGr;
    private Integer unidadesObjetivo;
    private BigDecimal kgProductoTerminado;
    private BigDecimal rendimientoTeoricoPct;
    private BigDecimal kgBatchFormula;
    private BigDecimal kgBatchCalculado;
    private BigDecimal numBachesCalculado;
    private String observaciones;

    public ProgramacionSkuResponse(
            Long id,
            Long idProgramacion,
            Long idSku,
            String codigoSku,
            String descripcionSku,
            Integer pesoUnidadGr,
            Integer unidadesObjetivo,
            BigDecimal kgProductoTerminado,
            BigDecimal rendimientoTeoricoPct,
            BigDecimal kgBatchFormula,
            BigDecimal kgBatchCalculado,
            BigDecimal numBachesCalculado,
            String observaciones) {
        this.id = id;
        this.idProgramacion = idProgramacion;
        this.idSku = idSku;
        this.codigoSku = codigoSku;
        this.descripcionSku = descripcionSku;
        this.pesoUnidadGr = pesoUnidadGr;
        this.unidadesObjetivo = unidadesObjetivo;
        this.kgProductoTerminado = kgProductoTerminado;
        this.rendimientoTeoricoPct = rendimientoTeoricoPct;
        this.kgBatchFormula = kgBatchFormula;
        this.kgBatchCalculado = kgBatchCalculado;
        this.numBachesCalculado = numBachesCalculado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public Long getIdSku() {
        return idSku;
    }

    public String getCodigoSku() {
        return codigoSku;
    }

    public String getDescripcionSku() {
        return descripcionSku;
    }

    public Integer getPesoUnidadGr() {
        return pesoUnidadGr;
    }

    public Integer getUnidadesObjetivo() {
        return unidadesObjetivo;
    }

    public BigDecimal getKgProductoTerminado() {
        return kgProductoTerminado;
    }

    public BigDecimal getRendimientoTeoricoPct() {
        return rendimientoTeoricoPct;
    }

    public BigDecimal getKgBatchFormula() {
        return kgBatchFormula;
    }

    public BigDecimal getKgBatchCalculado() {
        return kgBatchCalculado;
    }

    public BigDecimal getNumBachesCalculado() {
        return numBachesCalculado;
    }

    public String getObservaciones() {
        return observaciones;
    }
}