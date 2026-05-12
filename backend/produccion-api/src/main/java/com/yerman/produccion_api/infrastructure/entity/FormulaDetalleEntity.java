package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.TipoCalculoInsumo;
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

    @Column(name = "cantidad_kg", nullable = false, precision = 14, scale = 6)
    private BigDecimal cantidadKg;

    @Column(name = "porcentaje", precision = 10, scale = 6)
    private BigDecimal porcentaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_calculo", nullable = false, length = 30)
    private TipoCalculoInsumo tipoCalculo = TipoCalculoInsumo.PORCENTAJE_BATCH;

    @Column(name = "es_critico", nullable = false)
    private Boolean esCritico = false;

    @Column(name = "orden_adicion", nullable = false)
    private Integer ordenAdicion = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public FormulaDetalleEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FormulaVersionEntity getFormulaVersion() {
        return formulaVersion;
    }

    public void setFormulaVersion(FormulaVersionEntity formulaVersion) {
        this.formulaVersion = formulaVersion;
    }

    public InsumoEntity getInsumo() {
        return insumo;
    }

    public void setInsumo(InsumoEntity insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getCantidadKg() {
        return cantidadKg;
    }

    public void setCantidadKg(BigDecimal cantidadKg) {
        this.cantidadKg = cantidadKg;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public TipoCalculoInsumo getTipoCalculo() {
        return tipoCalculo;
    }

    public void setTipoCalculo(TipoCalculoInsumo tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    public Boolean getEsCritico() {
        return esCritico;
    }

    public void setEsCritico(Boolean esCritico) {
        this.esCritico = esCritico;
    }

    public Integer getOrdenAdicion() {
        return ordenAdicion;
    }

    public void setOrdenAdicion(Integer ordenAdicion) {
        this.ordenAdicion = ordenAdicion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}