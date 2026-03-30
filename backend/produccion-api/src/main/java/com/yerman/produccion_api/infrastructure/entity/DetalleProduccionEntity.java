package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "detalle_produccion", uniqueConstraints = {
        @UniqueConstraint(name = "uq_detalle_produccion", columnNames = { "id_produccion", "id_producto", "num_batch" })
})
public class DetalleProduccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_produccion")
    private Long idDetalleProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion", nullable = false)
    private ProduccionEntity produccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "kg_programados", nullable = false, precision = 10, scale = 2)
    private BigDecimal kgProgramados;

    @Column(name = "kg_batch", nullable = false, precision = 10, scale = 2)
    private BigDecimal kgBatch;

    @Column(name = "num_batch", nullable = false)
    private Integer numBatch;

    @Column(name = "unidades_reales", nullable = false)
    private Integer unidadesReales;

    @Column(name = "rendimiento_pct", precision = 5, scale = 2, insertable = false, updatable = false)
    private BigDecimal rendimientoPct;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "fecha_hora_registro", nullable = false)
    private LocalDateTime fechaHoraRegistro;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "detalleProduccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsumoInsumoEntity> consumosInsumo = new ArrayList<>();

    @OneToOne(mappedBy = "detalleProduccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ValidacionEntity validacion;

    @PrePersist
    protected void onCreate() {
        this.fechaHoraRegistro = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public ProduccionEntity getProduccion() {
        return produccion;
    }

    public void setProduccion(ProduccionEntity produccion) {
        this.produccion = produccion;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
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

    public Integer getNumBatch() {
        return numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public Integer getUnidadesReales() {
        return unidadesReales;
    }

    public void setUnidadesReales(Integer unidadesReales) {
        this.unidadesReales = unidadesReales;
    }

    public BigDecimal getRendimientoPct() {
        return rendimientoPct;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ConsumoInsumoEntity> getConsumosInsumo() {
        return consumosInsumo;
    }

    public void setConsumosInsumo(List<ConsumoInsumoEntity> consumosInsumo) {
        this.consumosInsumo = consumosInsumo;
    }

    public ValidacionEntity getValidacion() {
        return validacion;
    }

    public void setValidacion(ValidacionEntity validacion) {
        this.validacion = validacion;
    }
}