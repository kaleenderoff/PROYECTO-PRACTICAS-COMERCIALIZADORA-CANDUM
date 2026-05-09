package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "formula")
public class FormulaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private CatalogoProductoEntity producto;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "formula")
    private List<FormulaVersionEntity> versiones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public FormulaEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public CatalogoProductoEntity getProducto() {
        return producto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public List<FormulaVersionEntity> getVersiones() {
        return versiones;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProducto(CatalogoProductoEntity producto) {
        this.producto = producto;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setVersiones(List<FormulaVersionEntity> versiones) {
        this.versiones = versiones;
    }
}