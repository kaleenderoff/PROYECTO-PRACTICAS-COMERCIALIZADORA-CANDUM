package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class RecepcionLechePesajeResponse {

    private Long id;
    private Long idRecepcionLeche;
    private Integer numeroPesaje;
    private BigDecimal pesoBrutoKg;
    private BigDecimal taraKg;
    private BigDecimal pesoNetoKg;
    private String observaciones;

    public RecepcionLechePesajeResponse(Long id, Long idRecepcionLeche, Integer numeroPesaje,
            BigDecimal pesoBrutoKg, BigDecimal taraKg, BigDecimal pesoNetoKg, String observaciones) {
        this.id = id;
        this.idRecepcionLeche = idRecepcionLeche;
        this.numeroPesaje = numeroPesaje;
        this.pesoBrutoKg = pesoBrutoKg;
        this.taraKg = taraKg;
        this.pesoNetoKg = pesoNetoKg;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdRecepcionLeche() {
        return idRecepcionLeche;
    }

    public Integer getNumeroPesaje() {
        return numeroPesaje;
    }

    public BigDecimal getPesoBrutoKg() {
        return pesoBrutoKg;
    }

    public BigDecimal getTaraKg() {
        return taraKg;
    }

    public BigDecimal getPesoNetoKg() {
        return pesoNetoKg;
    }

    public String getObservaciones() {
        return observaciones;
    }
}