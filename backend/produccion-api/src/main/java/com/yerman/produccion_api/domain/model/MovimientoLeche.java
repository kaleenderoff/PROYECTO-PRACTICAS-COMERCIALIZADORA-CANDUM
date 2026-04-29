package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoLeche {

    private Long id;
    private Long idTanque;
    private TipoMovimientoLeche tipoMovimiento;
    private LocalDateTime fechaHora;
    private BigDecimal cantidadLitros;
    private BigDecimal saldoResultanteLitros;
    private Long idUsuario;
    private String referencia;
    private String observaciones;

    public MovimientoLeche() {
    }

    public MovimientoLeche(Long id, Long idTanque, TipoMovimientoLeche tipoMovimiento,
            LocalDateTime fechaHora, BigDecimal cantidadLitros, BigDecimal saldoResultanteLitros,
            Long idUsuario, String referencia, String observaciones) {
        this.id = id;
        this.idTanque = idTanque;
        this.tipoMovimiento = tipoMovimiento;
        this.fechaHora = fechaHora;
        this.cantidadLitros = cantidadLitros;
        this.saldoResultanteLitros = saldoResultanteLitros;
        this.idUsuario = idUsuario;
        this.referencia = referencia;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public TipoMovimientoLeche getTipoMovimiento() {
        return tipoMovimiento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public BigDecimal getCantidadLitros() {
        return cantidadLitros;
    }

    public BigDecimal getSaldoResultanteLitros() {
        return saldoResultanteLitros;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdTanque(Long idTanque) {
        this.idTanque = idTanque;
    }

    public void setTipoMovimiento(TipoMovimientoLeche tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setCantidadLitros(BigDecimal cantidadLitros) {
        this.cantidadLitros = cantidadLitros;
    }

    public void setSaldoResultanteLitros(BigDecimal saldoResultanteLitros) {
        this.saldoResultanteLitros = saldoResultanteLitros;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}