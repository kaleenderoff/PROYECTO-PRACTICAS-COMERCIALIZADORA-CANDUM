package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class ProduccionBatch {

    private Long id;
    private Long idProduccionLactea;
    private Integer numeroBatch;
    private Long idMarmita;
    private BigDecimal litrosConsumidos;
    private BigDecimal kilosProducidos;
    private BigDecimal rendimiento;
    private Long idMovimientoLeche;
    private String observaciones;

    public ProduccionBatch() {
    }

    public ProduccionBatch(Long id, Long idProduccionLactea, Integer numeroBatch, Long idMarmita,
            BigDecimal litrosConsumidos, BigDecimal kilosProducidos, BigDecimal rendimiento,
            Long idMovimientoLeche, String observaciones) {
        this.id = id;
        this.idProduccionLactea = idProduccionLactea;
        this.numeroBatch = numeroBatch;
        this.idMarmita = idMarmita;
        this.litrosConsumidos = litrosConsumidos;
        this.kilosProducidos = kilosProducidos;
        this.rendimiento = rendimiento;
        this.idMovimientoLeche = idMovimientoLeche;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduccionLactea() {
        return idProduccionLactea;
    }

    public Integer getNumeroBatch() {
        return numeroBatch;
    }

    public Long getIdMarmita() {
        return idMarmita;
    }

    public BigDecimal getLitrosConsumidos() {
        return litrosConsumidos;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public BigDecimal getRendimiento() {
        return rendimiento;
    }

    public Long getIdMovimientoLeche() {
        return idMovimientoLeche;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProduccionLactea(Long idProduccionLactea) {
        this.idProduccionLactea = idProduccionLactea;
    }

    public void setNumeroBatch(Integer numeroBatch) {
        this.numeroBatch = numeroBatch;
    }

    public void setIdMarmita(Long idMarmita) {
        this.idMarmita = idMarmita;
    }

    public void setLitrosConsumidos(BigDecimal litrosConsumidos) {
        this.litrosConsumidos = litrosConsumidos;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public void setRendimiento(BigDecimal rendimiento) {
        this.rendimiento = rendimiento;
    }

    public void setIdMovimientoLeche(Long idMovimientoLeche) {
        this.idMovimientoLeche = idMovimientoLeche;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}