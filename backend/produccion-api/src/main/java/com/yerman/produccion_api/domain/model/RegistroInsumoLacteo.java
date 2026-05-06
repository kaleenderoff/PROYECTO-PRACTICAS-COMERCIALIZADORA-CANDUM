package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroInsumoLacteo {

    private Long id;
    private Long idProduccionLactea;
    private Long idProduccionLacteaBatch;
    private Long idInsumo;
    private String loteInsumo;
    private BigDecimal cantidadRequerida;
    private BigDecimal cantidadUsada;
    private String unidadMedida;
    private LocalDateTime fechaHoraRegistro;
    private Long idUsuario;
    private String observaciones;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProduccionLactea(Long idProduccionLactea) {
        this.idProduccionLactea = idProduccionLactea;
    }

    public void setIdProduccionLacteaBatch(Long idProduccionLacteaBatch) {
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
    }

    public void setIdInsumo(Long idInsumo) {
        this.idInsumo = idInsumo;
    }

    public void setLoteInsumo(String loteInsumo) {
        this.loteInsumo = loteInsumo;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public void setCantidadUsada(BigDecimal cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
