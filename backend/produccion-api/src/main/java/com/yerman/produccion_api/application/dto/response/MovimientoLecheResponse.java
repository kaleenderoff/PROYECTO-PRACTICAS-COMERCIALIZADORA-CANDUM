package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoLecheResponse {

    private Long id;
    private Long idTanque;
    private String tipoMovimiento;
    private LocalDateTime fechaHora;
    private BigDecimal cantidadLitros;
    private BigDecimal saldoResultanteLitros;

    public MovimientoLecheResponse(
            Long id,
            Long idTanque,
            String tipoMovimiento,
            LocalDateTime fechaHora,
            BigDecimal cantidadLitros,
            BigDecimal saldoResultanteLitros) {
        this.id = id;
        this.idTanque = idTanque;
        this.tipoMovimiento = tipoMovimiento;
        this.fechaHora = fechaHora;
        this.cantidadLitros = cantidadLitros;
        this.saldoResultanteLitros = saldoResultanteLitros;
    }

    public Long getId() {
        return id;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public String getTipoMovimiento() {
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
}