package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumo_insumo", uniqueConstraints = {
        @UniqueConstraint(name = "uq_consumo_insumo", columnNames = { "id_produccion", "id_insumo",
                "id_detalle_produccion" })
})
public class ConsumoInsumoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo_insumo")
    private Long idConsumoInsumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion", nullable = false)
    private ProduccionEntity produccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false)
    private InsumoEntity insumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle_produccion", nullable = false)
    private DetalleProduccionEntity detalleProduccion;

    @Column(name = "cantidad_consumida", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidadConsumida;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getIdConsumoInsumo() {
        return idConsumoInsumo;
    }

    public void setIdConsumoInsumo(Long idConsumoInsumo) {
        this.idConsumoInsumo = idConsumoInsumo;
    }

    public ProduccionEntity getProduccion() {
        return produccion;
    }

    public void setProduccion(ProduccionEntity produccion) {
        this.produccion = produccion;
    }

    public InsumoEntity getInsumo() {
        return insumo;
    }

    public void setInsumo(InsumoEntity insumo) {
        this.insumo = insumo;
    }

    public DetalleProduccionEntity getDetalleProduccion() {
        return detalleProduccion;
    }

    public void setDetalleProduccion(DetalleProduccionEntity detalleProduccion) {
        this.detalleProduccion = detalleProduccion;
    }

    public BigDecimal getCantidadConsumida() {
        return cantidadConsumida;
    }

    public void setCantidadConsumida(BigDecimal cantidadConsumida) {
        this.cantidadConsumida = cantidadConsumida;
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
