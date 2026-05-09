package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "formula_detalle")
public class FormulaDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formula_version", nullable = false)
    private FormulaVersionEntity formulaVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false)
    private InsumoEntity insumo;

    @Column(name = "cantidad_kg", precision = 14, scale = 6)
    private BigDecimal cantidadKg;

    @Column(name = "porcentaje", precision = 10, scale = 6)
    private BigDecimal porcentaje;

    @Column(name = "es_critico")
    private Boolean esCritico;

    @Column(name = "orden_adicion")
    private Integer ordenAdicion;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public FormulaDetalleEntity() {
    }

    public Long getId() {
        return id;
    }

    public FormulaVersionEntity getFormulaVersion() {
        return formulaVersion;
    }

    public InsumoEntity getInsumo() {
        return insumo;
    }

    public BigDecimal getCantidadKg() {
        return cantidadKg;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public Boolean getEsCritico() {
        return esCritico;
    }

    public Integer getOrdenAdicion() {
        return ordenAdicion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFormulaVersion(FormulaVersionEntity formulaVersion) {
        this.formulaVersion = formulaVersion;
    }

    public void setInsumo(InsumoEntity insumo) {
        this.insumo = insumo;
    }

    public void setCantidadKg(BigDecimal cantidadKg) {
        this.cantidadKg = cantidadKg;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public void setEsCritico(Boolean esCritico) {
        this.esCritico = esCritico;
    }

    public void setOrdenAdicion(Integer ordenAdicion) {
        this.ordenAdicion = ordenAdicion;
    }
}