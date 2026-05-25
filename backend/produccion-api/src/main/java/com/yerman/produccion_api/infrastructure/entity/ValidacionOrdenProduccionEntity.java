package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "validacion_orden_produccion")
public class ValidacionOrdenProduccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenProduccionEntity orden;

    @Column(name = "aprobado", nullable = false)
    private Boolean aprobado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefe_produccion", nullable = false)
    private UsuarioEntity jefeProduccion;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @Column(name = "fecha_validacion", nullable = false)
    private LocalDateTime fechaValidacion;

    @Column(name = "requiere_revision", nullable = false)
    private Boolean requiereRevision;

    public Long getId() {
        return id;
    }

    public OrdenProduccionEntity getOrden() {
        return orden;
    }

    public void setOrden(OrdenProduccionEntity orden) {
        this.orden = orden;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public UsuarioEntity getJefeProduccion() {
        return jefeProduccion;
    }

    public void setJefeProduccion(UsuarioEntity jefeProduccion) {
        this.jefeProduccion = jefeProduccion;
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

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Boolean getRequiereRevision() {
        return requiereRevision;
    }

    public void setRequiereRevision(Boolean requiereRevision) {
        this.requiereRevision = requiereRevision;
    }
}
