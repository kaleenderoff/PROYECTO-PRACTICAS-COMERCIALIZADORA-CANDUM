package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardProduccionVsEmpaqueResponse {

    private LocalDate fecha;
    private BigDecimal totalProducido;
    private BigDecimal totalEmpaquetado;
    private BigDecimal diferencia;

    public DashboardProduccionVsEmpaqueResponse() {
    }

    public DashboardProduccionVsEmpaqueResponse(
            LocalDate fecha,
            BigDecimal totalProducido,
            BigDecimal totalEmpaquetado,
            BigDecimal diferencia) {
        this.fecha = fecha;
        this.totalProducido = totalProducido;
        this.totalEmpaquetado = totalEmpaquetado;
        this.diferencia = diferencia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotalProducido() {
        return totalProducido;
    }

    public void setTotalProducido(BigDecimal totalProducido) {
        this.totalProducido = totalProducido;
    }

    public BigDecimal getTotalEmpaquetado() {
        return totalEmpaquetado;
    }

    public void setTotalEmpaquetado(BigDecimal totalEmpaquetado) {
        this.totalEmpaquetado = totalEmpaquetado;
    }

    public BigDecimal getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {
        this.diferencia = diferencia;
    }
}