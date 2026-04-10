package com.yerman.produccion_api.application.dto.request;

import java.math.BigDecimal;

public class ProductoTerminadoRequest {

    private Long idProductoBase;
    private String sku;
    private String nombreComercial;
    private String referencia;
    private BigDecimal gramajeG;
    private String unidadMedida;
    private String embalaje;
    private Boolean activo;

    public ProductoTerminadoRequest() {
    }

    public Long getIdProductoBase() {
        return idProductoBase;
    }

    public void setIdProductoBase(Long idProductoBase) {
        this.idProductoBase = idProductoBase;
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

    public BigDecimal getGramajeG() {
        return gramajeG;
    }

    public void setGramajeG(BigDecimal gramajeG) {
        this.gramajeG = gramajeG;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(String embalaje) {
        this.embalaje = embalaje;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
