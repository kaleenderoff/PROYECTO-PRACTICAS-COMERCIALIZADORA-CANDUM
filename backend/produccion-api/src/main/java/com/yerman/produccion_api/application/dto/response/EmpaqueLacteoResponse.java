package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpaqueLacteoResponse {

    private Long id;
    private Long productoTerminadoLacteoId;

    private String loteEmpaque;
    private LocalDate fechaEmpaque;
    private LocalDate fechaVencimiento;

    private BigDecimal kilosUtilizados;
    private Integer unidades;
    private BigDecimal cajas;
    private BigDecimal pesoTotalKg;

    private String estado;
    private String observaciones;

    public EmpaqueLacteoResponse() {
    }

    public EmpaqueLacteoResponse(
            Long id,
            Long productoTerminadoLacteoId,
            String loteEmpaque,
            LocalDate fechaEmpaque,
            LocalDate fechaVencimiento,
            BigDecimal kilosUtilizados,
            Integer unidades,
            BigDecimal cajas,
            BigDecimal pesoTotalKg,
            String estado,
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

    public String getEstado() {
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

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}