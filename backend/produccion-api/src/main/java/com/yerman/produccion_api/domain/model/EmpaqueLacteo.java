package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpaqueLacteo {

    private Long id;
    private Long productoTerminadoLacteoId;

    private String loteEmpaque;
    private LocalDate fechaEmpaque;
    private LocalDate fechaVencimiento;

    private BigDecimal kilosUtilizados;
    private Integer unidades;
    private BigDecimal cajas;
    private BigDecimal pesoTotalKg;

    private EstadoEmpaqueLacteo estado;
    private String observaciones;

    public EmpaqueLacteo() {
    }

    public EmpaqueLacteo(
            Long id,
            Long productoTerminadoLacteoId,
            String loteEmpaque,
            LocalDate fechaEmpaque,
            LocalDate fechaVencimiento,
            BigDecimal kilosUtilizados,
            Integer unidades,
            BigDecimal cajas,
            BigDecimal pesoTotalKg,
            EstadoEmpaqueLacteo estado,
            String observaciones) {
        this.id = id;
        this.productoTerminadoLacteoId = productoTerminadoLacteoId;
        this.loteEmpaque = loteEmpaque;
        this.fechaEmpaque = fechaEmpaque;
        this.fechaVencimiento = fechaVencimiento;
        this.kilosUtilizados = kilosUtilizados;
        this.unidades = unidades;
        this.cajas = cajas;
        this.pesoTotalKg = pesoTotalKg;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getProductoTerminadoLacteoId() {
        return productoTerminadoLacteoId;
    }

    public String getLoteEmpaque() {
        return loteEmpaque;
    }

    public LocalDate getFechaEmpaque() {
        return fechaEmpaque;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public BigDecimal getKilosUtilizados() {
        return kilosUtilizados;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public BigDecimal getCajas() {
        return cajas;
    }

    public BigDecimal getPesoTotalKg() {
        return pesoTotalKg;
    }

    public EstadoEmpaqueLacteo getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductoTerminadoLacteoId(Long productoTerminadoLacteoId) {
        this.productoTerminadoLacteoId = productoTerminadoLacteoId;
    }

    public void setLoteEmpaque(String loteEmpaque) {
        this.loteEmpaque = loteEmpaque;
    }

    public void setFechaEmpaque(LocalDate fechaEmpaque) {
        this.fechaEmpaque = fechaEmpaque;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setKilosUtilizados(BigDecimal kilosUtilizados) {
        this.kilosUtilizados = kilosUtilizados;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public void setCajas(BigDecimal cajas) {
        this.cajas = cajas;
    }

    public void setPesoTotalKg(BigDecimal pesoTotalKg) {
        this.pesoTotalKg = pesoTotalKg;
    }

    public void setEstado(EstadoEmpaqueLacteo estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}