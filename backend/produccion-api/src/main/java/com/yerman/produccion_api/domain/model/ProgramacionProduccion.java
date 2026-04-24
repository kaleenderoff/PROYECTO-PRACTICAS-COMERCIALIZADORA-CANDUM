package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProgramacionProduccion {

    private Long id;
    private String codigoProgramacion;
    private LocalDate fechaProduccion;

    private Long idLinea;
    private Long idProducto;
    private Long idTurno;

    private Integer numBachesPlan;
    private BigDecimal kgBachePlan;

    private Long idFormulaVersion;
    private Long idJefeProduccion;

    private EstadoProgramacionProduccion estado;
    private String observaciones;

    public ProgramacionProduccion() {
    }

    public ProgramacionProduccion(Long id, String codigoProgramacion, LocalDate fechaProduccion,
            Long idLinea, Long idProducto, Long idTurno,
            Integer numBachesPlan, BigDecimal kgBachePlan,
            Long idFormulaVersion, Long idJefeProduccion,
            EstadoProgramacionProduccion estado, String observaciones) {
        this.id = id;
        this.codigoProgramacion = codigoProgramacion;
        this.fechaProduccion = fechaProduccion;
        this.idLinea = idLinea;
        this.idProducto = idProducto;
        this.idTurno = idTurno;
        this.numBachesPlan = numBachesPlan;
        this.kgBachePlan = kgBachePlan;
        this.idFormulaVersion = idFormulaVersion;
        this.idJefeProduccion = idJefeProduccion;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoProgramacion() {
        return codigoProgramacion;
    }

    public void setCodigoProgramacion(String codigoProgramacion) {
        this.codigoProgramacion = codigoProgramacion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
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

    public Long getIdFormulaVersion() {
        return idFormulaVersion;
    }

    public void setIdFormulaVersion(Long idFormulaVersion) {
        this.idFormulaVersion = idFormulaVersion;
    }

    public Long getIdJefeProduccion() {
        return idJefeProduccion;
    }

    public void setIdJefeProduccion(Long idJefeProduccion) {
        this.idJefeProduccion = idJefeProduccion;
    }

    public EstadoProgramacionProduccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoProgramacionProduccion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}