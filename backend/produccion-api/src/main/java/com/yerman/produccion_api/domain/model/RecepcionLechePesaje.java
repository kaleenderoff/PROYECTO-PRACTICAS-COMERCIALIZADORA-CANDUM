package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class RecepcionLechePesaje {

    private Long id;
    private Long idRecepcionLeche;
    private Integer numeroPesaje;
    private BigDecimal pesoBrutoKg;
    private BigDecimal taraKg;
    private BigDecimal pesoNetoKg;
    private String observaciones;

    public RecepcionLechePesaje() {
    }

    public RecepcionLechePesaje(Long id, Long idRecepcionLeche, Integer numeroPesaje,
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdRecepcionLeche(Long idRecepcionLeche) {
        this.idRecepcionLeche = idRecepcionLeche;
    }

    public void setNumeroPesaje(Integer numeroPesaje) {
        this.numeroPesaje = numeroPesaje;
    }

    public void setPesoBrutoKg(BigDecimal pesoBrutoKg) {
        this.pesoBrutoKg = pesoBrutoKg;
    }

    public void setTaraKg(BigDecimal taraKg) {
        this.taraKg = taraKg;
    }

    public void setPesoNetoKg(BigDecimal pesoNetoKg) {
        this.pesoNetoKg = pesoNetoKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}