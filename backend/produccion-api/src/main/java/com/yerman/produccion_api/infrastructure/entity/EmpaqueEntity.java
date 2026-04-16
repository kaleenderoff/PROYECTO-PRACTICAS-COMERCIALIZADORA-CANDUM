package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.EstadoEmpaque;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "empaque", uniqueConstraints = {
        @UniqueConstraint(name = "uq_empaque_detalle_producto_lote", columnNames = {
                "id_detalle_produccion", "id_producto_terminado", "lote_empaque"
        })
})
public class EmpaqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empaque")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle_produccion", nullable = false)
    private DetalleProduccionEntity detalleProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto_terminado", nullable = false)
    private ProductoTerminadoEntity productoTerminado;

    @Column(name = "lote_empaque", nullable = false, length = 50)
    private String loteEmpaque;

    @Column(name = "fecha_empaque", nullable = false)
    private LocalDateTime fechaEmpaque;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoEmpaque estado;

    @Column(name = "cantidad_unidades", nullable = false)
    private Integer cantidadUnidades;

    @Column(name = "cantidad_cajas")
    private Integer cantidadCajas;

    @Column(name = "peso_total_kg", precision = 10, scale = 2)
    private BigDecimal pesoTotalKg;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public EmpaqueEntity() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime ahora = LocalDateTime.now();

        if (this.fechaEmpaque == null) {
            this.fechaEmpaque = ahora;
        }

        if (this.createdAt == null) {
            this.createdAt = ahora;
        }

        this.updatedAt = ahora;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public DetalleProduccionEntity getDetalleProduccion() {
        return detalleProduccion;
    }

    public ProductoTerminadoEntity getProductoTerminado() {
        return productoTerminado;
    }

    public String getLoteEmpaque() {
        return loteEmpaque;
    }

    public LocalDateTime getFechaEmpaque() {
        return fechaEmpaque;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public EstadoEmpaque getEstado() {
        return estado;
    }

    public Integer getCantidadUnidades() {
        return cantidadUnidades;
    }

    public Integer getCantidadCajas() {
        return cantidadCajas;
    }

    public BigDecimal getPesoTotalKg() {
        return pesoTotalKg;
    }

    public String getObservaciones() {
        return observaciones;
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

    public void setDetalleProduccion(DetalleProduccionEntity detalleProduccion) {
        this.detalleProduccion = detalleProduccion;
    }

    public void setProductoTerminado(ProductoTerminadoEntity productoTerminado) {
        this.productoTerminado = productoTerminado;
    }

    public void setLoteEmpaque(String loteEmpaque) {
        this.loteEmpaque = loteEmpaque;
    }

    public void setFechaEmpaque(LocalDateTime fechaEmpaque) {
        this.fechaEmpaque = fechaEmpaque;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setEstado(EstadoEmpaque estado) {
        this.estado = estado;
    }

    public void setCantidadUnidades(Integer cantidadUnidades) {
        this.cantidadUnidades = cantidadUnidades;
    }

    public void setCantidadCajas(Integer cantidadCajas) {
        this.cantidadCajas = cantidadCajas;
    }

    public void setPesoTotalKg(BigDecimal pesoTotalKg) {
        this.pesoTotalKg = pesoTotalKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}