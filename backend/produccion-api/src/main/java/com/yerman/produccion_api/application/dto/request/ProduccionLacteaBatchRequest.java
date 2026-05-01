package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProduccionLacteaBatchRequest {

    @NotNull
    private Integer numeroBatch;

    @NotNull
    private Long idMarmita;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal litrosConsumidos;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal kilosProducidos;

    @Size(max = 500)
    private String observaciones;

    public Integer getNumeroBatch() {
        return numeroBatch;
    }

    public void setNumeroBatch(Integer numeroBatch) {
        this.numeroBatch = numeroBatch;
    }

    public Long getIdMarmita() {
        return idMarmita;
    }

    public void setIdMarmita(Long idMarmita) {
        this.idMarmita = idMarmita;
    }

    public BigDecimal getLitrosConsumidos() {
        return litrosConsumidos;
    }

    public void setLitrosConsumidos(BigDecimal litrosConsumidos) {
        this.litrosConsumidos = litrosConsumidos;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}