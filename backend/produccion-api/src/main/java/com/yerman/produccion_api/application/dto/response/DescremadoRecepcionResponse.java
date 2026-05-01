package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class DescremadoRecepcionResponse {

    private Long id;
    private Long idRecepcionLeche;
    private BigDecimal litrosDescremados;
    private BigDecimal cremaObtenidaKg;
    private String observaciones;

    public DescremadoRecepcionResponse(Long id, Long idRecepcionLeche,
            BigDecimal litrosDescremados, BigDecimal cremaObtenidaKg,
            String observaciones) {
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
}