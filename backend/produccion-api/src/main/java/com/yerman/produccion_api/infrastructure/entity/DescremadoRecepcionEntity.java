package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "descremado_recepcion")
public class DescremadoRecepcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recepcion_leche", nullable = false)
    private RecepcionLecheEntity recepcionLeche;

    @Column(name = "litros_descremados", nullable = false, precision = 14, scale = 3)
    private BigDecimal litrosDescremados;

    @Column(name = "crema_obtenida_kg", precision = 14, scale = 3)
    private BigDecimal cremaObtenidaKg;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public DescremadoRecepcionEntity() {
    }

    public Long getId() {
        return id;
    }

    public RecepcionLecheEntity getRecepcionLeche() {
        return recepcionLeche;
    }

    public BigDecimal getLitrosDescremados() {
        return litrosDescremados;
    }

    public BigDecimal getCremaObtenidaKg() {
        return cremaObtenidaKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecepcionLeche(RecepcionLecheEntity recepcionLeche) {
        this.recepcionLeche = recepcionLeche;
    }

    public void setLitrosDescremados(BigDecimal litrosDescremados) {
        this.litrosDescremados = litrosDescremados;
    }

    public void setCremaObtenidaKg(BigDecimal cremaObtenidaKg) {
        this.cremaObtenidaKg = cremaObtenidaKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}