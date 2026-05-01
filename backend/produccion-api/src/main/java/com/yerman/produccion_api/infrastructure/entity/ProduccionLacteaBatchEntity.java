package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produccion_lactea_batch")
public class ProduccionLacteaBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion_lactea", nullable = false)
    private ProduccionLacteaEntity produccion;

    @Column(name = "numero_batch", nullable = false)
    private Integer numeroBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marmita", nullable = false)
    private MarmitaEntity marmita;

    @Column(name = "litros_consumidos", nullable = false, precision = 14, scale = 3)
    private BigDecimal litrosConsumidos;

    @Column(name = "kilos_producidos", nullable = false, precision = 14, scale = 3)
    private BigDecimal kilosProducidos;

    @Column(name = "rendimiento", precision = 10, scale = 4)
    private BigDecimal rendimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimiento_leche")
    private MovimientoLecheEntity movimientoLeche;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public ProduccionLacteaBatchEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProduccionLacteaEntity getProduccion() {
        return produccion;
    }

    public Integer getNumeroBatch() {
        return numeroBatch;
    }

    public MarmitaEntity getMarmita() {
        return marmita;
    }

    public BigDecimal getLitrosConsumidos() {
        return litrosConsumidos;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public BigDecimal getRendimiento() {
        return rendimiento;
    }

    public MovimientoLecheEntity getMovimientoLeche() {
        return movimientoLeche;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduccion(ProduccionLacteaEntity produccion) {
        this.produccion = produccion;
    }

    public void setNumeroBatch(Integer numeroBatch) {
        this.numeroBatch = numeroBatch;
    }

    public void setMarmita(MarmitaEntity marmita) {
        this.marmita = marmita;
    }

    public void setLitrosConsumidos(BigDecimal litrosConsumidos) {
        this.litrosConsumidos = litrosConsumidos;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public void setRendimiento(BigDecimal rendimiento) {
        this.rendimiento = rendimiento;
    }

    public void setMovimientoLeche(MovimientoLecheEntity movimientoLeche) {
        this.movimientoLeche = movimientoLeche;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}