package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "formula_version")
public class FormulaVersionEntity {

    public enum EstadoFormula {
        BORRADOR,
        APROBADA,
        VIGENTE,
        REEMPLAZADA,
        INACTIVA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formula", nullable = false)
    private FormulaEntity formula;

    @Column(name = "version", nullable = false, length = 20)
    private String version;

    @Column(name = "fecha_inicio_vigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "fecha_fin_vigencia")
    private LocalDate fechaFinVigencia;

    @Column(name = "kg_batch_total", precision = 10, scale = 2)
    private BigDecimal kgBatchTotal;

    @Column(name = "reduccion_evaporacion_pct", precision = 6, scale = 4)
    private BigDecimal reduccionEvaporacionPct;

    @Column(name = "rendimiento_teorico_pct", precision = 6, scale = 4)
    private BigDecimal rendimientoTeoricoPct;

    @Column(name = "brix_objetivo_min", precision = 5, scale = 2)
    private BigDecimal brixObjetivoMin;

    @Column(name = "brix_objetivo_max", precision = 5, scale = 2)
    private BigDecimal brixObjetivoMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoFormula estado;

    @Column(name = "aprobado_por", length = 100)
    private String aprobadoPor;

    @Column(name = "documento_aprobacion", length = 500)
    private String documentoAprobacion;

    @Column(name = "observaciones_tecnicas", columnDefinition = "TEXT")
    private String observacionesTecnicas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creado_por", nullable = false)
    private UsuarioEntity creadoPor;

    @OneToMany(mappedBy = "formulaVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormulaDetalleEntity> detalles;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public FormulaVersionEntity() {
    }

    public Long getId() {
        return id;
    }

    public FormulaEntity getFormula() {
        return formula;
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public LocalDate getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public BigDecimal getKgBatchTotal() {
        return kgBatchTotal;
    }

    public BigDecimal getReduccionEvaporacionPct() {
        return reduccionEvaporacionPct;
    }

    public BigDecimal getRendimientoTeoricoPct() {
        return rendimientoTeoricoPct;
    }

    public BigDecimal getBrixObjetivoMin() {
        return brixObjetivoMin;
    }

    public BigDecimal getBrixObjetivoMax() {
        return brixObjetivoMax;
    }

    public EstadoFormula getEstado() {
        return estado;
    }

    public String getAprobadoPor() {
        return aprobadoPor;
    }

    public String getDocumentoAprobacion() {
        return documentoAprobacion;
    }

    public String getObservacionesTecnicas() {
        return observacionesTecnicas;
    }

    public UsuarioEntity getCreadoPor() {
        return creadoPor;
    }

    public List<FormulaDetalleEntity> getDetalles() {
        return detalles;
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

    public void setFormula(FormulaEntity formula) {
        this.formula = formula;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setFechaInicioVigencia(LocalDate fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public void setFechaFinVigencia(LocalDate fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    public void setKgBatchTotal(BigDecimal kgBatchTotal) {
        this.kgBatchTotal = kgBatchTotal;
    }

    public void setReduccionEvaporacionPct(BigDecimal reduccionEvaporacionPct) {
        this.reduccionEvaporacionPct = reduccionEvaporacionPct;
    }

    public void setRendimientoTeoricoPct(BigDecimal rendimientoTeoricoPct) {
        this.rendimientoTeoricoPct = rendimientoTeoricoPct;
    }

    public void setBrixObjetivoMin(BigDecimal brixObjetivoMin) {
        this.brixObjetivoMin = brixObjetivoMin;
    }

    public void setBrixObjetivoMax(BigDecimal brixObjetivoMax) {
        this.brixObjetivoMax = brixObjetivoMax;
    }

    public void setEstado(EstadoFormula estado) {
        this.estado = estado;
    }

    public void setAprobadoPor(String aprobadoPor) {
        this.aprobadoPor = aprobadoPor;
    }

    public void setDocumentoAprobacion(String documentoAprobacion) {
        this.documentoAprobacion = documentoAprobacion;
    }

    public void setObservacionesTecnicas(String observacionesTecnicas) {
        this.observacionesTecnicas = observacionesTecnicas;
    }

    public void setCreadoPor(UsuarioEntity creadoPor) {
        this.creadoPor = creadoPor;
    }

    public void setDetalles(List<FormulaDetalleEntity> detalles) {
        this.detalles = detalles;
    }
}