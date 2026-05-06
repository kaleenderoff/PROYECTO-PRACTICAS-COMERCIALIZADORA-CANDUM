package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroInsumoLacteoResponse {

    private final Long id;
    private final Long idProduccionLactea;
    private final Long idProduccionLacteaBatch;
    private final Long idInsumo;
    private final String loteInsumo;
    private final BigDecimal cantidadRequerida;
    private final BigDecimal cantidadUsada;
    private final String unidadMedida;
    private final LocalDateTime fechaHoraRegistro;
    private final Long idUsuario;
    private final String observaciones;

    public RegistroInsumoLacteoResponse(Long id, Long idProduccionLactea, Long idProduccionLacteaBatch,
            Long idInsumo, String loteInsumo, BigDecimal cantidadRequerida, BigDecimal cantidadUsada,
            String unidadMedida, LocalDateTime fechaHoraRegistro, Long idUsuario, String observaciones) {
        this.id = id;
        this.idProduccionLactea = idProduccionLactea;
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
        this.idInsumo = idInsumo;
        this.loteInsumo = loteInsumo;
        this.cantidadRequerida = cantidadRequerida;
        this.cantidadUsada = cantidadUsada;
        this.unidadMedida = unidadMedida;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduccionLactea() {
        return idProduccionLactea;
    }

    public Long getIdProduccionLacteaBatch() {
        return idProduccionLacteaBatch;
    }

    public Long getIdInsumo() {
        return idInsumo;
    }

    public String getLoteInsumo() {
        return loteInsumo;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public BigDecimal getCantidadUsada() {
        return cantidadUsada;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
