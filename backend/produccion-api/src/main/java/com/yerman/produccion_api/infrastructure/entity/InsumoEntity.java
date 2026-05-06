package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "insumo")
public class InsumoEntity {

    public enum TipoInsumo {
        MATERIA_PRIMA,
        MATERIAL_EMPAQUE,
        ADITIVO,
        OTRO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoInsumo tipo;

    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida;

    @Column(name = "stock_minimo", precision = 14, scale = 3)
    private BigDecimal stockMinimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private ProveedorEntity proveedor;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoInsumo getTipo() {
        return tipo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
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

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipo(TipoInsumo tipo) {
        this.tipo = tipo;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
