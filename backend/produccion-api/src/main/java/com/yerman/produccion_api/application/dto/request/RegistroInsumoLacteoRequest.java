package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroInsumoLacteoRequest {

    @NotNull
    private Long idProduccionLactea;

    private Long idProduccionLacteaBatch;

    @NotNull
    private Long idInsumo;

    @Size(max = 100)
    private String loteInsumo;

    @DecimalMin(value = "0.00")
    private BigDecimal cantidadRequerida;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal cantidadUsada;

    @NotBlank
    @Size(max = 50)
    private String unidadMedida;

    private LocalDateTime fechaHoraRegistro;

    @NotNull
    private Long idUsuario;

    @Size(max = 500)
    private String observaciones;

    public Long getIdProduccionLactea() {
        return idProduccionLactea;
    }

    public void setIdProduccionLactea(Long idProduccionLactea) {
        this.idProduccionLactea = idProduccionLactea;
    }

    public Long getIdProduccionLacteaBatch() {
        return idProduccionLacteaBatch;
    }

    public void setIdProduccionLacteaBatch(Long idProduccionLacteaBatch) {
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
    }

    public Long getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Long idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getLoteInsumo() {
        return loteInsumo;
    }

    public void setLoteInsumo(String loteInsumo) {
        this.loteInsumo = loteInsumo;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public BigDecimal getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(BigDecimal cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
