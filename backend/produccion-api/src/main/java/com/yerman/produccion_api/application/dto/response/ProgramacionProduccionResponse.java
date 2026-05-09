package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProgramacionProduccionResponse {

    private Long id;
    private String codigoProgramacion;
    private LocalDate fechaProduccion;

    private Long idLinea;
    private String nombreLinea;

    private Long idProducto;
    private String nombreProducto;

    private Long idTurno;
    private String nombreTurno;

    private Integer numBachesPlan;
    private BigDecimal kgBachePlan;

    private Long idFormulaVersion;
    private String nombreFormula;
    private String versionFormula;
    private BigDecimal rendimientoTeoricoPct;
    private BigDecimal kgBatchFormula;

    private Long idJefeProduccion;
    private String nombreJefeProduccion;

    private String estado;
    private String observaciones;

    private List<ProgramacionSkuResponse> skus;

    private BigDecimal totalKgProductoTerminado;
    private BigDecimal totalKgBatchCalculado;
    private BigDecimal totalNumBachesCalculado;

    public ProgramacionProduccionResponse(
            Long id,
            String codigoProgramacion,
            LocalDate fechaProduccion,
            Long idLinea,
            String nombreLinea,
            Long idProducto,
            String nombreProducto,
            Long idTurno,
            String nombreTurno,
            Integer numBachesPlan,
            BigDecimal kgBachePlan,
            Long idFormulaVersion,
            String nombreFormula,
            String versionFormula,
            BigDecimal rendimientoTeoricoPct,
            BigDecimal kgBatchFormula,
            Long idJefeProduccion,
            String nombreJefeProduccion,
            String estado,
            String observaciones,
            List<ProgramacionSkuResponse> skus,
            BigDecimal totalKgProductoTerminado,
            BigDecimal totalKgBatchCalculado,
            BigDecimal totalNumBachesCalculado) {
        this.id = id;
        this.codigoProgramacion = codigoProgramacion;
        this.fechaProduccion = fechaProduccion;
        this.idLinea = idLinea;
        this.nombreLinea = nombreLinea;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.idTurno = idTurno;
        this.nombreTurno = nombreTurno;
        this.numBachesPlan = numBachesPlan;
        this.kgBachePlan = kgBachePlan;
        this.idFormulaVersion = idFormulaVersion;
        this.nombreFormula = nombreFormula;
        this.versionFormula = versionFormula;
        this.rendimientoTeoricoPct = rendimientoTeoricoPct;
        this.kgBatchFormula = kgBatchFormula;
        this.idJefeProduccion = idJefeProduccion;
        this.nombreJefeProduccion = nombreJefeProduccion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.skus = skus;
        this.totalKgProductoTerminado = totalKgProductoTerminado;
        this.totalKgBatchCalculado = totalKgBatchCalculado;
        this.totalNumBachesCalculado = totalNumBachesCalculado;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoProgramacion() {
        return codigoProgramacion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public String getNombreLinea() {
        return nombreLinea;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public Long getIdTurno() {
        return idTurno;
    }

    public String getNombreTurno() {
        return nombreTurno;
    }

    public Integer getNumBachesPlan() {
        return numBachesPlan;
    }

    public BigDecimal getKgBachePlan() {
        return kgBachePlan;
    }

    public Long getIdFormulaVersion() {
        return idFormulaVersion;
    }

    public String getNombreFormula() {
        return nombreFormula;
    }

    public String getVersionFormula() {
        return versionFormula;
    }

    public BigDecimal getRendimientoTeoricoPct() {
        return rendimientoTeoricoPct;
    }

    public BigDecimal getKgBatchFormula() {
        return kgBatchFormula;
    }

    public Long getIdJefeProduccion() {
        return idJefeProduccion;
    }

    public String getNombreJefeProduccion() {
        return nombreJefeProduccion;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<ProgramacionSkuResponse> getSkus() {
        return skus;
    }

    public BigDecimal getTotalKgProductoTerminado() {
        return totalKgProductoTerminado;
    }

    public BigDecimal getTotalKgBatchCalculado() {
        return totalKgBatchCalculado;
    }

    public BigDecimal getTotalNumBachesCalculado() {
        return totalNumBachesCalculado;
    }
}