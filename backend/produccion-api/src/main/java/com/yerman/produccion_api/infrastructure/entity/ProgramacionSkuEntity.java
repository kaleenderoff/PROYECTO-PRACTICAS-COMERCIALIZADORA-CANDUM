package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "programacion_sku", uniqueConstraints = {
        @UniqueConstraint(name = "uq_programacion_detalle_sku", columnNames = { "id_programacion", "id_sku" })
})
public class ProgramacionSkuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programacion", nullable = false)
    private ProgramacionProduccionEntity programacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sku", nullable = false)
    private CatalogoSkuEntity sku;

    @Column(name = "unidades_objetivo", nullable = false)
    private Integer unidadesObjetivo;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public ProgramacionSkuEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProgramacionProduccionEntity getProgramacion() {
        return programacion;
    }

    public void setProgramacion(ProgramacionProduccionEntity programacion) {
        this.programacion = programacion;
    }

    public CatalogoSkuEntity getSku() {
        return sku;
    }

    public void setSku(CatalogoSkuEntity sku) {
        this.sku = sku;
    }

    public Integer getUnidadesObjetivo() {
        return unidadesObjetivo;
    }

    public void setUnidadesObjetivo(Integer unidadesObjetivo) {
        this.unidadesObjetivo = unidadesObjetivo;
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