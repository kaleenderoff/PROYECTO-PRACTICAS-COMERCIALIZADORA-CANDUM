package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recepcion_leche_pesaje")
public class RecepcionLechePesajeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recepcion_leche", nullable = false)
    private RecepcionLecheEntity recepcionLeche;

    @Column(name = "numero_pesaje", nullable = false)
    private Integer numeroPesaje;

    @Column(name = "peso_bruto_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal pesoBrutoKg;

    @Column(name = "tara_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal taraKg;

    @Column(name = "peso_neto_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal pesoNetoKg;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public RecepcionLechePesajeEntity() {
    }

    public Long getId() {
        return id;
    }

    public RecepcionLecheEntity getRecepcionLeche() {
        return recepcionLeche;
    }

    public Integer getNumeroPesaje() {
        return numeroPesaje;
    }

    public BigDecimal getPesoBrutoKg() {
        return pesoBrutoKg;
    }

    public BigDecimal getTaraKg() {
        return taraKg;
    }

    public BigDecimal getPesoNetoKg() {
        return pesoNetoKg;
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

    public void setNumeroPesaje(Integer numeroPesaje) {
        this.numeroPesaje = numeroPesaje;
    }

    public void setPesoBrutoKg(BigDecimal pesoBrutoKg) {
        this.pesoBrutoKg = pesoBrutoKg;
    }

    public void setTaraKg(BigDecimal taraKg) {
        this.taraKg = taraKg;
    }

    public void setPesoNetoKg(BigDecimal pesoNetoKg) {
        this.pesoNetoKg = pesoNetoKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}