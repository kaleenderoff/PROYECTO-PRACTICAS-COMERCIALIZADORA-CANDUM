package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class DashboardProduccionVsEmpaqueResponse {

    private Long idDetalleProduccion;
    private Long idProduccion;
    private String numeroLoteProduccion;
    private Long idProducto;
    private String nombreProducto;
    private Integer numBatch;
    private BigDecimal kgProgramados;
    private BigDecimal kgBatch;
    private long unidadesReales;
    private long unidadesEmpacadas;
    private long cajasEmpacadas;
    private BigDecimal pesoEmpacadoKg;
    private long unidadesPendientesPorEmpacar;

    public DashboardProduccionVsEmpaqueResponse() {
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public Long getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Long idProduccion) {
        this.idProduccion = idProduccion;
    }

    public String getNumeroLoteProduccion() {
        return numeroLoteProduccion;
    }

    public void setNumeroLoteProduccion(String numeroLoteProduccion) {
        this.numeroLoteProduccion = numeroLoteProduccion;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getNumBatch() {
        return numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public BigDecimal getKgProgramados() {
        return kgProgramados;
    }

    public void setKgProgramados(BigDecimal kgProgramados) {
        this.kgProgramados = kgProgramados;
    }

    public BigDecimal getKgBatch() {
        return kgBatch;
    }

    public void setKgBatch(BigDecimal kgBatch) {
        this.kgBatch = kgBatch;
    }

    public long getUnidadesReales() {
        return unidadesReales;
    }

    public void setUnidadesReales(long unidadesReales) {
        this.unidadesReales = unidadesReales;
    }

    public long getUnidadesEmpacadas() {
        return unidadesEmpacadas;
    }

    public void setUnidadesEmpacadas(long unidadesEmpacadas) {
        this.unidadesEmpacadas = unidadesEmpacadas;
    }

    public long getCajasEmpacadas() {
        return cajasEmpacadas;
    }

    public void setCajasEmpacadas(long cajasEmpacadas) {
        this.cajasEmpacadas = cajasEmpacadas;
    }

    public BigDecimal getPesoEmpacadoKg() {
        return pesoEmpacadoKg;
    }

    public void setPesoEmpacadoKg(BigDecimal pesoEmpacadoKg) {
        this.pesoEmpacadoKg = pesoEmpacadoKg;
    }

    public long getUnidadesPendientesPorEmpacar() {
        return unidadesPendientesPorEmpacar;
    }

    public void setUnidadesPendientesPorEmpacar(long unidadesPendientesPorEmpacar) {
        this.unidadesPendientesPorEmpacar = unidadesPendientesPorEmpacar;
    }
}