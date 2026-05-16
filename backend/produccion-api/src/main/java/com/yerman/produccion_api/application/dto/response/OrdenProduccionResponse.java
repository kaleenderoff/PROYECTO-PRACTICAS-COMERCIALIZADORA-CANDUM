package com.yerman.produccion_api.application.dto.response;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;

public class OrdenProduccionResponse {

    private Long id;
    private String numeroOrden;

    private Long idProgramacion;

    private Long idLinea;
    private String nombreLinea;

    private Long idProducto;
    private String nombreProducto;

    private Long idTurno;
    private String nombreTurno;

    private Long idJefeLineaEjecutor;
    private String nombreJefeLineaEjecutor;

    private Long idCreadaPor;
    private String nombreCreadaPor;

    private LocalDate fechaProduccion;
    private EstadoOrdenProduccion estado;
    private String observaciones;
    private LocalDateTime fechaInicioReal;
    private LocalDateTime fechaFinReal;
    private BigDecimal kgEntradaReal;
    private BigDecimal kgProducidoBatches;
    private BigDecimal kgPtReal;
    private BigDecimal rendimientoReal;
    private BigDecimal mermaReal;
    private BigDecimal mermaEmpaque;
    private Long idTanqueLeche;
    private String nombreTanqueLeche;

    // Resumen Operativo
    private Integer numBachesPlan;
    private BigDecimal kgBachePlan;
    private BigDecimal kgPTTotalPlan;
    private BigDecimal kgEntradaTotalPlan;
    private String nombreFormula;
    private String versionFormula;

    // Detalle SKUs
    private List<ProgramacionSkuResponse> skus;

    public OrdenProduccionResponse() {
    }

    public OrdenProduccionResponse(
            Long id,
            String numeroOrden,
            Long idProgramacion,
            Long idLinea,
            String nombreLinea,
            Long idProducto,
            String nombreProducto,
            Long idTurno,
            String nombreTurno,
            Long idJefeLineaEjecutor,
            String nombreJefeLineaEjecutor,
            Long idCreadaPor,
            String nombreCreadaPor,
            LocalDate fechaProduccion,
            EstadoOrdenProduccion estado,
            String observaciones,
            LocalDateTime fechaInicioReal,
            LocalDateTime fechaFinReal,
            Integer numBachesPlan,
            BigDecimal kgBachePlan,
            BigDecimal kgPTTotalPlan,
            BigDecimal kgEntradaTotalPlan,
            String nombreFormula,
            String versionFormula,
            List<ProgramacionSkuResponse> skus) {

        this.id = id;
        this.numeroOrden = numeroOrden;
        this.idProgramacion = idProgramacion;
        this.idLinea = idLinea;
        this.nombreLinea = nombreLinea;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.idTurno = idTurno;
        this.nombreTurno = nombreTurno;
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
        this.nombreJefeLineaEjecutor = nombreJefeLineaEjecutor;
        this.idCreadaPor = idCreadaPor;
        this.nombreCreadaPor = nombreCreadaPor;
        this.fechaProduccion = fechaProduccion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaInicioReal = fechaInicioReal;
        this.fechaFinReal = fechaFinReal;
        this.numBachesPlan = numBachesPlan;
        this.kgBachePlan = kgBachePlan;
        this.kgPTTotalPlan = kgPTTotalPlan;
        this.kgEntradaTotalPlan = kgEntradaTotalPlan;
        this.nombreFormula = nombreFormula;
        this.versionFormula = versionFormula;
        this.skus = skus;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public Long getIdProgramacion() {
        return idProgramacion;
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

    public Long getIdJefeLineaEjecutor() {
        return idJefeLineaEjecutor;
    }

    public String getNombreJefeLineaEjecutor() {
        return nombreJefeLineaEjecutor;
    }

    public Long getIdCreadaPor() {
        return idCreadaPor;
    }

    public String getNombreCreadaPor() {
        return nombreCreadaPor;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public EstadoOrdenProduccion getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaInicioReal() {
        return fechaInicioReal;
    }

    public LocalDateTime getFechaFinReal() {
        return fechaFinReal;
    }

    public Integer getNumBachesPlan() {
        return numBachesPlan;
    }

    public BigDecimal getKgBachePlan() {
        return kgBachePlan;
    }

    public BigDecimal getKgPTTotalPlan() {
        return kgPTTotalPlan;
    }

    public BigDecimal getKgEntradaTotalPlan() {
        return kgEntradaTotalPlan;
    }

    public String getNombreFormula() {
        return nombreFormula;
    }

    public String getVersionFormula() {
        return versionFormula;
    }

    public List<ProgramacionSkuResponse> getSkus() {
        return skus;
    }

    public BigDecimal getKgEntradaReal() {
        return kgEntradaReal;
    }

    public void setKgEntradaReal(BigDecimal kgEntradaReal) {
        this.kgEntradaReal = kgEntradaReal;
    }

    public BigDecimal getKgPtReal() {
        return kgPtReal;
    }

    public void setKgPtReal(BigDecimal kgPtReal) {
        this.kgPtReal = kgPtReal;
    }

    public BigDecimal getRendimientoReal() {
        return rendimientoReal;
    }

    public void setRendimientoReal(BigDecimal rendimientoReal) {
        this.rendimientoReal = rendimientoReal;
    }

    public BigDecimal getMermaReal() {
        return mermaReal;
    }

    public void setMermaReal(BigDecimal mermaReal) {
        this.mermaReal = mermaReal;
    }

    public BigDecimal getKgProducidoBatches() {
        return kgProducidoBatches;
    }

    public void setKgProducidoBatches(BigDecimal kgProducidoBatches) {
        this.kgProducidoBatches = kgProducidoBatches;
    }

    public BigDecimal getMermaEmpaque() {
        return mermaEmpaque;
    }

    public void setMermaEmpaque(BigDecimal mermaEmpaque) {
        this.mermaEmpaque = mermaEmpaque;
    }

    public Long getIdTanqueLeche() {
        return idTanqueLeche;
    }

    public void setIdTanqueLeche(Long idTanqueLeche) {
        this.idTanqueLeche = idTanqueLeche;
    }

    public String getNombreTanqueLeche() {
        return nombreTanqueLeche;
    }

    public void setNombreTanqueLeche(String nombreTanqueLeche) {
        this.nombreTanqueLeche = nombreTanqueLeche;
    }
}