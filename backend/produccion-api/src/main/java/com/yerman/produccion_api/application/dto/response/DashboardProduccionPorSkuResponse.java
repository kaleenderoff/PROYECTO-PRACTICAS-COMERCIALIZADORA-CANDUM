package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class DashboardProduccionPorSkuResponse {

    private Long idProductoTerminado;
    private String sku;
    private String nombreComercial;
    private String referencia;
    private long totalUnidades;
    private long totalCajas;
    private BigDecimal totalPesoKg;
    private long totalRegistrosEmpaque;

    public DashboardProduccionPorSkuResponse() {
    }

    public Long getIdProductoTerminado() {
        return idProductoTerminado;
    }

    public void setIdProductoTerminado(Long idProductoTerminado) {
        this.idProductoTerminado = idProductoTerminado;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public long getTotalUnidades() {
        return totalUnidades;
    }

    public void setTotalUnidades(long totalUnidades) {
        this.totalUnidades = totalUnidades;
    }

    public long getTotalCajas() {
        return totalCajas;
    }

    public void setTotalCajas(long totalCajas) {
        this.totalCajas = totalCajas;
    }

    public BigDecimal getTotalPesoKg() {
        return totalPesoKg;
    }

    public void setTotalPesoKg(BigDecimal totalPesoKg) {
        this.totalPesoKg = totalPesoKg;
    }

    public long getTotalRegistrosEmpaque() {
        return totalRegistrosEmpaque;
    }

    public void setTotalRegistrosEmpaque(long totalRegistrosEmpaque) {
        this.totalRegistrosEmpaque = totalRegistrosEmpaque;
    }
}
