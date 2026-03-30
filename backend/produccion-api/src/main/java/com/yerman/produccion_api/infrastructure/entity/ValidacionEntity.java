package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "validacion", uniqueConstraints = {
        @UniqueConstraint(name = "uq_validacion_detalle", columnNames = "id_detalle_produccion")
})
public class ValidacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validacion")
    private Long idValidacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_detalle_produccion", nullable = false, unique = true)
    private DetalleProduccionEntity detalleProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_validador", nullable = false)
    private UsuarioEntity validador;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @Column(name = "fecha_validacion", nullable = false)
    private LocalDateTime fechaValidacion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.fechaValidacion = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getIdValidacion() {
        return idValidacion;
    }

    public void setIdValidacion(Long idValidacion) {
        this.idValidacion = idValidacion;
    }

    public DetalleProduccionEntity getDetalleProduccion() {
        return detalleProduccion;
    }

    public void setDetalleProduccion(DetalleProduccionEntity detalleProduccion) {
        this.detalleProduccion = detalleProduccion;
    }

    public UsuarioEntity getValidador() {
        return validador;
    }

    public void setValidador(UsuarioEntity validador) {
        this.validador = validador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}