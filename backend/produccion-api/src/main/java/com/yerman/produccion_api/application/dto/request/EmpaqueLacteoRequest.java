package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpaqueLacteoRequest {

    @NotNull
    private Long productoTerminadoLacteoId;

    @NotNull(message = "El batch es obligatorio")
    private Long produccionLacteaBatchId;

    private Long skuId;

    @NotBlank
    private String loteEmpaque;

    @NotNull
    private LocalDate fechaEmpaque;

    private LocalDate fechaVencimiento;

    @NotNull
    @DecimalMin("0.001")
    private BigDecimal kilosUtilizados;

    @NotNull
    @Min(1)
    private Integer unidades;

    private BigDecimal cajas;

    @NotNull
    @DecimalMin("0.001")
    private BigDecimal pesoTotalKg;

    private String observaciones;

    public EmpaqueLacteoRequest() {
    }

    public Long getProductoTerminadoLacteoId() {
        return productoTerminadoLacteoId;
    }

    public Long getProduccionLacteaBatchId() {
        return produccionLacteaBatchId;
    }

    public Long getSkuId() {
        return skuId;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setProductoTerminadoLacteoId(Long v) {
        this.productoTerminadoLacteoId = v;
    }

    public void setProduccionLacteaBatchId(Long v) {
        this.produccionLacteaBatchId = v;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public void setLoteEmpaque(String v) {
        this.loteEmpaque = v;
    }

    public void setFechaEmpaque(LocalDate v) {
        this.fechaEmpaque = v;
    }

    public void setFechaVencimiento(LocalDate v) {
        this.fechaVencimiento = v;
    }

    public void setKilosUtilizados(BigDecimal v) {
        this.kilosUtilizados = v;
    }

    public void setUnidades(Integer v) {
        this.unidades = v;
    }

    public void setCajas(BigDecimal v) {
        this.cajas = v;
    }

    public void setPesoTotalKg(BigDecimal v) {
        this.pesoTotalKg = v;
    }

    public void setObservaciones(String v) {
        this.observaciones = v;
    }
}
