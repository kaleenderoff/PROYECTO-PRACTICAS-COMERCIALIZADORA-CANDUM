package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpaqueLacteoRequest {

    @NotNull(message = "El producto terminado lácteo es obligatorio")
    private Long productoTerminadoLacteoId;

    @NotBlank(message = "El lote de empaque es obligatorio")
    @Size(max = 80, message = "El lote de empaque no puede superar los 80 caracteres")
    private String loteEmpaque;

    @NotNull(message = "La fecha de empaque es obligatoria")
    private LocalDate fechaEmpaque;

    private LocalDate fechaVencimiento;

    @NotNull(message = "Los kilos utilizados son obligatorios")
    @DecimalMin(value = "0.001", message = "Los kilos utilizados deben ser mayores a 0")
    private BigDecimal kilosUtilizados;

    @NotNull(message = "Las unidades son obligatorias")
    @Min(value = 1, message = "Las unidades deben ser mayores a 0")
    private Integer unidades;

    @DecimalMin(value = "0.001", message = "Las cajas deben ser mayores a 0")
    private BigDecimal cajas;

    @NotNull(message = "El peso total en kg es obligatorio")
    @DecimalMin(value = "0.001", message = "El peso total en kg debe ser mayor a 0")
    private BigDecimal pesoTotalKg;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    public EmpaqueLacteoRequest() {
    }

    public Long getProductoTerminadoLacteoId() {
        return productoTerminadoLacteoId;
    }

    public void setProductoTerminadoLacteoId(Long productoTerminadoLacteoId) {
        this.productoTerminadoLacteoId = productoTerminadoLacteoId;
    }

    public String getLoteEmpaque() {
        return loteEmpaque;
    }

    public void setLoteEmpaque(String loteEmpaque) {
        this.loteEmpaque = loteEmpaque;
    }

    public LocalDate getFechaEmpaque() {
        return fechaEmpaque;
    }

    public void setFechaEmpaque(LocalDate fechaEmpaque) {
        this.fechaEmpaque = fechaEmpaque;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getKilosUtilizados() {
        return kilosUtilizados;
    }

    public void setKilosUtilizados(BigDecimal kilosUtilizados) {
        this.kilosUtilizados = kilosUtilizados;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public BigDecimal getCajas() {
        return cajas;
    }

    public void setCajas(BigDecimal cajas) {
        this.cajas = cajas;
    }

    public BigDecimal getPesoTotalKg() {
        return pesoTotalKg;
    }

    public void setPesoTotalKg(BigDecimal pesoTotalKg) {
        this.pesoTotalKg = pesoTotalKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}