package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class DescremadoRecepcion {

    private Long id;
    private Long idRecepcionLeche;
    private BigDecimal litrosDescremados;
    private BigDecimal cremaObtenidaKg;
    private String observaciones;

    public DescremadoRecepcion() {
    }

    public DescremadoRecepcion(Long id, Long idRecepcionLeche, BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg, String observaciones) {
        this.id = id;
        this.idRecepcionLeche = idRecepcionLeche;
        this.litrosDescremados = litrosDescremados;
        this.cremaObtenidaKg = cremaObtenidaKg;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdRecepcionLeche() {
        return idRecepcionLeche;
    }

    public BigDecimal getLitrosDescremados() {
        return litrosDescremados;
    }

    public BigDecimal getCremaObtenidaKg() {
        return cremaObtenidaKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdRecepcionLeche(Long idRecepcionLeche) {
        this.idRecepcionLeche = idRecepcionLeche;
    }

    public void setLitrosDescremados(BigDecimal litrosDescremados) {
        this.litrosDescremados = litrosDescremados;
    }

    public void setCremaObtenidaKg(BigDecimal cremaObtenidaKg) {
        this.cremaObtenidaKg = cremaObtenidaKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}