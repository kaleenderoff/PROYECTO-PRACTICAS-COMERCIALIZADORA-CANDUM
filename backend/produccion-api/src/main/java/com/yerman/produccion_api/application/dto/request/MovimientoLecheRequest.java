package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class MovimientoLecheRequest {

    @NotNull
    private Long idTanque;

    @NotNull
    private Long idUsuario;

    @NotNull
    private String tipoMovimiento;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal cantidadLitros;

    @Size(max = 100)
    private String referencia;

    @Size(max = 500)
    private String observaciones;

    public Long getIdTanque() {
        return idTanque;
    }

    public void setIdTanque(Long idTanque) {
        this.idTanque = idTanque;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getCantidadLitros() {
        return cantidadLitros;
    }

    public void setCantidadLitros(BigDecimal cantidadLitros) {
        this.cantidadLitros = cantidadLitros;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}