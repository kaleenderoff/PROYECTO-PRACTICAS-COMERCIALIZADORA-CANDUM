package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto_terminado_lacteo")
public class ProductoTerminadoLacteoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion_lactea_batch", nullable = false)
    private ProduccionLacteaBatchEntity produccionLacteaBatch;

    @Column(name = "producto", nullable = false, length = 120)
    private String producto;

    @Column(name = "lote", length = 80)
    private String lote;

    @Column(name = "kilos_producidos", nullable = false, precision = 14, scale = 3)
    private BigDecimal kilosProducidos;

    @Column(name = "kilos_disponibles", nullable = false, precision = 14, scale = 3)
    private BigDecimal kilosDisponibles;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoProductoTerminadoLacteo estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public ProductoTerminadoLacteoEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProduccionLacteaBatchEntity getProduccionLacteaBatch() {
        return produccionLacteaBatch;
    }

    public String getProducto() {
        return producto;
    }

    public String getLote() {
        return lote;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public BigDecimal getKilosDisponibles() {
        return kilosDisponibles;
    }

    public EstadoProductoTerminadoLacteo getEstado() {
        return estado;
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

    public void setProduccionLacteaBatch(ProduccionLacteaBatchEntity produccionLacteaBatch) {
        this.produccionLacteaBatch = produccionLacteaBatch;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public void setKilosDisponibles(BigDecimal kilosDisponibles) {
        this.kilosDisponibles = kilosDisponibles;
    }

    public void setEstado(EstadoProductoTerminadoLacteo estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}