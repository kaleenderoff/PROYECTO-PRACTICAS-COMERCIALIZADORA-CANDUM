package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto", uniqueConstraints = {
        @UniqueConstraint(name = "uq_producto", columnNames = { "nombre", "gramaje_g", "marca" })
})
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "gramaje_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal gramajeG;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_linea_produccion", nullable = false)
    private LineaProduccionEntity lineaProduccion;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<DetalleProduccionEntity> detallesProduccion = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getGramajeG() {
        return gramajeG;
    }

    public void setGramajeG(BigDecimal gramajeG) {
        this.gramajeG = gramajeG;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LineaProduccionEntity getLineaProduccion() {
        return lineaProduccion;
    }

    public void setLineaProduccion(LineaProduccionEntity lineaProduccion) {
        this.lineaProduccion = lineaProduccion;
    }

    public List<DetalleProduccionEntity> getDetallesProduccion() {
        return detallesProduccion;
    }

    public void setDetallesProduccion(List<DetalleProduccionEntity> detallesProduccion) {
        this.detallesProduccion = detallesProduccion;
    }
}
