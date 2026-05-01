package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class DescremadoRecepcionRequest {

    @NotNull
    private Long idRecepcionLeche;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal litrosDescremados;

    @DecimalMin(value = "0.000")
    private BigDecimal cremaObtenidaKg;

    @Size(max = 500)
    private String observaciones;

    public Long getIdRecepcionLeche() {
        return idRecepcionLeche;
    }

    public void setIdRecepcionLeche(Long idRecepcionLeche) {
        this.idRecepcionLeche = idRecepcionLeche;
    }

    public BigDecimal getLitrosDescremados() {
        return litrosDescremados;
    }

    public void setLitrosDescremados(BigDecimal litrosDescremados) {
        this.litrosDescremados = litrosDescremados;
    }

    public BigDecimal getCremaObtenidaKg() {
        return cremaObtenidaKg;
    }

    public void setCremaObtenidaKg(BigDecimal cremaObtenidaKg) {
        this.cremaObtenidaKg = cremaObtenidaKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}