package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "produccion_lactea")
public class ProduccionLacteaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_produccion", nullable = false)
    private LocalDate fechaProduccion;

    @Column(name = "producto", nullable = false, length = 120)
    private String producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tanque", nullable = false)
    private TanqueLecheEntity tanque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @OneToMany(mappedBy = "produccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProduccionLacteaBatchEntity> batches;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public ProduccionLacteaEntity() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public String getProducto() {
        return producto;
    }

    public TanqueLecheEntity getTanque() {
        return tanque;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<ProduccionLacteaBatchEntity> getBatches() {
        return batches;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setTanque(TanqueLecheEntity tanque) {
        this.tanque = tanque;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setBatches(List<ProduccionLacteaBatchEntity> batches) {
        this.batches = batches;
    }
}