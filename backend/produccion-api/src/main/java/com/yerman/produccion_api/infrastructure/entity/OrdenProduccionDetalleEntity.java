package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_produccion_detalle", uniqueConstraints = {
        @UniqueConstraint(name = "uq_orden_detalle_sku", columnNames = { "id_orden", "id_sku" })
})
public class OrdenProduccionDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenProduccionEntity orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programacion_sku")
    private ProgramacionSkuEntity programacionSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sku", nullable = false)
    private CatalogoSkuEntity sku;

    @Column(name = "cantidad_programada", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadProgramada;

    @Column(name = "unidad_programada", nullable = false, length = 50)
    private String unidadProgramada;

    @Column(name = "prioridad", nullable = false)
    private Integer prioridad;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public OrdenProduccionDetalleEntity() {
    }

    public Long getId() {
        return id;
    }

    public OrdenProduccionEntity getOrden() {
        return orden;
    }

    public void setOrden(OrdenProduccionEntity orden) {
        this.orden = orden;
    }

    public ProgramacionSkuEntity getProgramacionSku() {
        return programacionSku;
    }

    public void setProgramacionSku(ProgramacionSkuEntity programacionSku) {
        this.programacionSku = programacionSku;
    }

    public CatalogoSkuEntity getSku() {
        return sku;
    }

    public void setSku(CatalogoSkuEntity sku) {
        this.sku = sku;
    }

    public BigDecimal getCantidadProgramada() {
        return cantidadProgramada;
    }

    public void setCantidadProgramada(BigDecimal cantidadProgramada) {
        this.cantidadProgramada = cantidadProgramada;
    }

    public String getUnidadProgramada() {
        return unidadProgramada;
    }

    public void setUnidadProgramada(String unidadProgramada) {
        this.unidadProgramada = unidadProgramada;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}