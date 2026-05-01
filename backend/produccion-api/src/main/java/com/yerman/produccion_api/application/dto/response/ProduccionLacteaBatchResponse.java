package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class ProduccionLacteaBatchResponse {

    private Long id;
    private Integer numeroBatch;
    private Long idMarmita;
    private BigDecimal litrosConsumidos;
    private BigDecimal kilosProducidos;
    private BigDecimal rendimiento;
    private Long idMovimientoLeche;

    public ProduccionLacteaBatchResponse(Long id, Integer numeroBatch, Long idMarmita,
            BigDecimal litrosConsumidos, BigDecimal kilosProducidos,
            BigDecimal rendimiento, Long idMovimientoLeche) {
        this.id = id;
        this.numeroBatch = numeroBatch;
        this.idMarmita = idMarmita;
        this.litrosConsumidos = litrosConsumidos;
        this.kilosProducidos = kilosProducidos;
        this.rendimiento = rendimiento;
        this.idMovimientoLeche = idMovimientoLeche;
    }

    public Long getId() {
        return id;
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
}