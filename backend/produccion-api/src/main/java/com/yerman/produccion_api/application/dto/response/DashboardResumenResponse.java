package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class DashboardResumenResponse {

    private long totalProducciones;
    private long totalDetallesProduccion;
    private long totalValidaciones;
    private long totalEmpaques;
    private long totalProductosTerminados;
    private BigDecimal totalKgProgramados;
    private BigDecimal totalKgBatch;
    private long totalUnidadesReales;
    private long totalUnidadesEmpacadas;
    private long totalCajasEmpacadas;
    private BigDecimal totalPesoEmpacadoKg;

    public DashboardResumenResponse() {
    }

    public long getTotalProducciones() {
        return totalProducciones;
    }

    public void setTotalProducciones(long totalProducciones) {
        this.totalProducciones = totalProducciones;
    }

    public long getTotalDetallesProduccion() {
        return totalDetallesProduccion;
    }

    public void setTotalDetallesProduccion(long totalDetallesProduccion) {
        this.totalDetallesProduccion = totalDetallesProduccion;
    }

    public long getTotalValidaciones() {
        return totalValidaciones;
    }

    public void setTotalValidaciones(long totalValidaciones) {
        this.totalValidaciones = totalValidaciones;
    }

    public long getTotalEmpaques() {
        return totalEmpaques;
    }

    public void setTotalEmpaques(long totalEmpaques) {
        this.totalEmpaques = totalEmpaques;
    }

    public long getTotalProductosTerminados() {
        return totalProductosTerminados;
    }

    public void setTotalProductosTerminados(long totalProductosTerminados) {
        this.totalProductosTerminados = totalProductosTerminados;
    }

    public BigDecimal getTotalKgProgramados() {
        return totalKgProgramados;
    }

    public void setTotalKgProgramados(BigDecimal totalKgProgramados) {
        this.totalKgProgramados = totalKgProgramados;
    }

    public BigDecimal getTotalKgBatch() {
        return totalKgBatch;
    }

    public void setTotalKgBatch(BigDecimal totalKgBatch) {
        this.totalKgBatch = totalKgBatch;
    }

    public long getTotalUnidadesReales() {
        return totalUnidadesReales;
    }

    public void setTotalUnidadesReales(long totalUnidadesReales) {
        this.totalUnidadesReales = totalUnidadesReales;
    }

    public long getTotalUnidadesEmpacadas() {
        return totalUnidadesEmpacadas;
    }

    public void setTotalUnidadesEmpacadas(long totalUnidadesEmpacadas) {
        this.totalUnidadesEmpacadas = totalUnidadesEmpacadas;
    }

    public long getTotalCajasEmpacadas() {
        return totalCajasEmpacadas;
    }

    public void setTotalCajasEmpacadas(long totalCajasEmpacadas) {
        this.totalCajasEmpacadas = totalCajasEmpacadas;
    }

    public BigDecimal getTotalPesoEmpacadoKg() {
        return totalPesoEmpacadoKg;
    }

    public void setTotalPesoEmpacadoKg(BigDecimal totalPesoEmpacadoKg) {
        this.totalPesoEmpacadoKg = totalPesoEmpacadoKg;
    }
}