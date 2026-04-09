package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto_terminado")
public class ProductoTerminadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_terminado")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "sku", nullable = false, length = 50, unique = true)
    private String sku;

    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "gramaje_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal gramajeG;

    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida;

    @Column(name = "embalaje", length = 100)
    private String embalaje;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ProductoTerminadoEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProductoEntity getProducto() {
        return producto;
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

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
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