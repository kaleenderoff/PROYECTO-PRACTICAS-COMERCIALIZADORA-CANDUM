package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class RecepcionLechePesajeRequest {

    @NotNull
    private Integer numeroPesaje;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal pesoBrutoKg;

    @NotNull
    @DecimalMin(value = "0.000")
    private BigDecimal taraKg;

    @Size(max = 300)
    private String observaciones;

    public Integer getNumeroPesaje() {
        return numeroPesaje;
    }

    public void setNumeroPesaje(Integer numeroPesaje) {
        this.numeroPesaje = numeroPesaje;
    }

    public BigDecimal getPesoBrutoKg() {
        return pesoBrutoKg;
    }

    public void setPesoBrutoKg(BigDecimal pesoBrutoKg) {
        this.pesoBrutoKg = pesoBrutoKg;
    }

    public BigDecimal getTaraKg() {
        return taraKg;
    }

    public void setTaraKg(BigDecimal taraKg) {
        this.taraKg = taraKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}