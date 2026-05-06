package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class DescremadoRecepcionRequest {

    @NotNull
    private Long idRecepcionLeche;

    @NotNull
    private Long idTanqueDestino;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal litrosDescremados;

    @DecimalMin(value = "0.000")
    private BigDecimal cremaObtenidaKg;

    private Long idSkuCrema;

    @DecimalMin(value = "1")
    private Integer unidadesCrema;

    @DecimalMin(value = "0.001")
    private BigDecimal kgPorUnidadCrema;

    @Size(max = 80)
    private String loteCrema;

    @Size(max = 500)
    private String observaciones;

    public Long getIdRecepcionLeche() {
        return idRecepcionLeche;
    }

    public void setIdRecepcionLeche(Long idRecepcionLeche) {
        this.idRecepcionLeche = idRecepcionLeche;
    }

    public Long getIdTanqueDestino() {
        return idTanqueDestino;
    }

    public void setIdTanqueDestino(Long idTanqueDestino) {
        this.idTanqueDestino = idTanqueDestino;
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

    public Long getIdSkuCrema() {
        return idSkuCrema;
    }

    public void setIdSkuCrema(Long idSkuCrema) {
        this.idSkuCrema = idSkuCrema;
    }

    public Integer getUnidadesCrema() {
        return unidadesCrema;
    }

    public void setUnidadesCrema(Integer unidadesCrema) {
        this.unidadesCrema = unidadesCrema;
    }

    public BigDecimal getKgPorUnidadCrema() {
        return kgPorUnidadCrema;
    }

    public void setKgPorUnidadCrema(BigDecimal kgPorUnidadCrema) {
        this.kgPorUnidadCrema = kgPorUnidadCrema;
    }

    public String getLoteCrema() {
        return loteCrema;
    }

    public void setLoteCrema(String loteCrema) {
        this.loteCrema = loteCrema;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
