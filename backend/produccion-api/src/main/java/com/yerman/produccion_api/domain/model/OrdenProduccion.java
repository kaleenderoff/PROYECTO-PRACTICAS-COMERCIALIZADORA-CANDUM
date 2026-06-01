package com.yerman.produccion_api.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;

public class OrdenProduccion {

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
    private Long idTanqueLeche;
    private String nombreTanqueLeche;

    // Cierre de tandas de calidad
    private Boolean tandasCerradas;
    private LocalDateTime fechaCierreTandas;
    private Long idUsuarioCierreTandas;
    private String nombreUsuarioCierreTandas;
    private String observacionesCierreTandas;

    // Metricas Reales
    private BigDecimal kgEntradaReal;
    private BigDecimal kgProducidoBatches;
    private BigDecimal kgPtReal;
    private BigDecimal rendimientoReal;
    private BigDecimal mermaReal;
    private BigDecimal mermaEmpaque;

    // Resumen Operativo
    private Integer numBachesPlan;
    private BigDecimal kgBachePlan;
    private BigDecimal kgPTTotalPlan;
    private BigDecimal kgEntradaTotalPlan;
    private String nombreFormula;
    private String versionFormula;

    // Detalle SKUs
    private List<ProgramacionSku> skus;

    public OrdenProduccion() {
    }

    public OrdenProduccion(
            Long id,
            String numeroOrden,
            Long idProgramacion,
            Long idLinea,
            Long idProducto,
            Long idTurno,
            Long idJefeLineaEjecutor,
            Long idCreadaPor,
            LocalDate fechaProduccion,
            EstadoOrdenProduccion estado,
            String observaciones,
            LocalDateTime fechaInicioReal,
            LocalDateTime fechaFinReal,
            Long idTanqueLeche) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.idProgramacion = idProgramacion;
        this.idLinea = idLinea;
        this.idProducto = idProducto;
        this.idTurno = idTurno;
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
        this.idCreadaPor = idCreadaPor;
        this.fechaProduccion = fechaProduccion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaInicioReal = fechaInicioReal;
        this.fechaFinReal = fechaFinReal;
        this.idTanqueLeche = idTanqueLeche;
        this.tandasCerradas = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public void setIdProgramacion(Long idProgramacion) {
        this.idProgramacion = idProgramacion;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public String getNombreLinea() {
        return nombreLinea;
    }

    public void setNombreLinea(String nombreLinea) {
        this.nombreLinea = nombreLinea;
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

    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public String getNombreTurno() {
        return nombreTurno;
    }

    public void setNombreTurno(String nombreTurno) {
        this.nombreTurno = nombreTurno;
    }

    public Long getIdJefeLineaEjecutor() {
        return idJefeLineaEjecutor;
    }

    public void setIdJefeLineaEjecutor(Long idJefeLineaEjecutor) {
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
    }

    public String getNombreJefeLineaEjecutor() {
        return nombreJefeLineaEjecutor;
    }

    public void setNombreJefeLineaEjecutor(String nombreJefeLineaEjecutor) {
        this.nombreJefeLineaEjecutor = nombreJefeLineaEjecutor;
    }

    public Long getIdCreadaPor() {
        return idCreadaPor;
    }

    public void setIdCreadaPor(Long idCreadaPor) {
        this.idCreadaPor = idCreadaPor;
    }

    public String getNombreCreadaPor() {
        return nombreCreadaPor;
    }

    public void setNombreCreadaPor(String nombreCreadaPor) {
        this.nombreCreadaPor = nombreCreadaPor;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public EstadoOrdenProduccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrdenProduccion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaInicioReal() {
        return fechaInicioReal;
    }

    public void setFechaInicioReal(LocalDateTime fechaInicioReal) {
        this.fechaInicioReal = fechaInicioReal;
    }

    public LocalDateTime getFechaFinReal() {
        return fechaFinReal;
    }

    public void setFechaFinReal(LocalDateTime fechaFinReal) {
        this.fechaFinReal = fechaFinReal;
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

    public Boolean getTandasCerradas() {
        return tandasCerradas;
    }

    public void setTandasCerradas(Boolean tandasCerradas) {
        this.tandasCerradas = tandasCerradas;
    }

    public LocalDateTime getFechaCierreTandas() {
        return fechaCierreTandas;
    }

    public void setFechaCierreTandas(LocalDateTime fechaCierreTandas) {
        this.fechaCierreTandas = fechaCierreTandas;
    }

    public Long getIdUsuarioCierreTandas() {
        return idUsuarioCierreTandas;
    }

    public void setIdUsuarioCierreTandas(Long idUsuarioCierreTandas) {
        this.idUsuarioCierreTandas = idUsuarioCierreTandas;
    }

    public String getNombreUsuarioCierreTandas() {
        return nombreUsuarioCierreTandas;
    }

    public void setNombreUsuarioCierreTandas(String nombreUsuarioCierreTandas) {
        this.nombreUsuarioCierreTandas = nombreUsuarioCierreTandas;
    }

    public String getObservacionesCierreTandas() {
        return observacionesCierreTandas;
    }

    public void setObservacionesCierreTandas(String observacionesCierreTandas) {
        this.observacionesCierreTandas = observacionesCierreTandas;
    }

    public Integer getNumBachesPlan() {
        return numBachesPlan;
    }

    public void setNumBachesPlan(Integer numBachesPlan) {
        this.numBachesPlan = numBachesPlan;
    }

    public BigDecimal getKgBachePlan() {
        return kgBachePlan;
    }

    public void setKgBachePlan(BigDecimal kgBachePlan) {
        this.kgBachePlan = kgBachePlan;
    }

    public BigDecimal getKgPTTotalPlan() {
        return kgPTTotalPlan;
    }

    public void setKgPTTotalPlan(BigDecimal kgPTTotalPlan) {
        this.kgPTTotalPlan = kgPTTotalPlan;
    }

    public BigDecimal getKgEntradaTotalPlan() {
        return kgEntradaTotalPlan;
    }

    public void setKgEntradaTotalPlan(BigDecimal kgEntradaTotalPlan) {
        this.kgEntradaTotalPlan = kgEntradaTotalPlan;
    }

    public String getNombreFormula() {
        return nombreFormula;
    }

    public void setNombreFormula(String nombreFormula) {
        this.nombreFormula = nombreFormula;
    }

    public String getVersionFormula() {
        return versionFormula;
    }

    public void setVersionFormula(String versionFormula) {
        this.versionFormula = versionFormula;
    }

    public List<ProgramacionSku> getSkus() {
        return skus;
    }

    public void setSkus(List<ProgramacionSku> skus) {
        this.skus = skus;
    }

    public BigDecimal getKgEntradaReal() {
        return kgEntradaReal;
    }

    public void setKgEntradaReal(BigDecimal kgEntradaReal) {
        this.kgEntradaReal = kgEntradaReal;
    }

    public BigDecimal getKgProducidoBatches() {
        return kgProducidoBatches;
    }

    public void setKgProducidoBatches(BigDecimal kgProducidoBatches) {
        this.kgProducidoBatches = kgProducidoBatches;
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

    public BigDecimal getMermaEmpaque() {
        return mermaEmpaque;
    }

    public void setMermaEmpaque(BigDecimal mermaEmpaque) {
        this.mermaEmpaque = mermaEmpaque;
    }
}