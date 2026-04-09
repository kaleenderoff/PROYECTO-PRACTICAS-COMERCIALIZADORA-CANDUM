package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoTerminado {

    private Long id;
    private Producto productoBase;
    private String sku;
    private String nombreComercial;
    private String referencia;
    private BigDecimal gramajeG;
    private String unidadMedida;
    private String embalaje;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductoTerminado() {
    }

    public ProductoTerminado(Long id, Producto productoBase, String sku, String nombreComercial,
            String referencia, BigDecimal gramajeG, String unidadMedida,
            String embalaje, Boolean activo,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productoBase = productoBase;
        this.sku = sku;
        this.nombreComercial = nombreComercial;
        this.referencia = referencia;
        this.gramajeG = gramajeG;
        this.unidadMedida = unidadMedida;
        this.embalaje = embalaje;
        this.activo = activo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Producto getProductoBase() {
        return productoBase;
    }

    public String getSku() {
        return sku;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getReferencia() {
        return referencia;
    }

    public BigDecimal getGramajeG() {
        return gramajeG;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public String getEmbalaje() {
        return embalaje;
    }

    public Boolean getActivo() {
        return activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductoBase(Producto productoBase) {
        this.productoBase = productoBase;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setGramajeG(BigDecimal gramajeG) {
        this.gramajeG = gramajeG;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setEmbalaje(String embalaje) {
        this.embalaje = embalaje;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
