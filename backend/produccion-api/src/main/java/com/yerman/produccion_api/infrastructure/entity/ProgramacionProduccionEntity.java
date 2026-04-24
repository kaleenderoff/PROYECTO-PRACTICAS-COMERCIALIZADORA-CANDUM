package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.yerman.produccion_api.domain.model.EstadoProgramacionProduccion;

@Entity
@Table(name = "programacion_produccion", uniqueConstraints = {
        @UniqueConstraint(name = "uq_programacion_linea_turno_fecha_producto", columnNames = { "fecha_produccion",
                "id_linea", "id_turno", "id_producto" }),
        @UniqueConstraint(name = "uq_programacion_codigo", columnNames = { "codigo_programacion" })
})
public class ProgramacionProduccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_programacion", nullable = false, length = 40)
    private String codigoProgramacion;

    @Column(name = "fecha_produccion", nullable = false)
    private LocalDate fechaProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_linea", nullable = false)
    private CatalogoLineaEntity linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private CatalogoProductoEntity producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turno", nullable = false)
    private TurnoEntity turno;

    @Column(name = "num_baches_plan")
    private Integer numBachesPlan;

    @Column(name = "kg_bache_plan", precision = 10, scale = 2)
    private BigDecimal kgBachePlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formula_version", nullable = false)
    private FormulaVersionEntity formulaVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefe_produccion", nullable = false)
    private UsuarioEntity jefeProduccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('BORRADOR','CONFIRMADA','EN_EJECUCION','CERRADA','CANCELADA')")
    private EstadoProgramacionProduccion estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public ProgramacionProduccionEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getCodigoProgramacion() {
        return codigoProgramacion;
    }

    public void setCodigoProgramacion(String codigoProgramacion) {
        this.codigoProgramacion = codigoProgramacion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public CatalogoLineaEntity getLinea() {
        return linea;
    }

    public void setLinea(CatalogoLineaEntity linea) {
        this.linea = linea;
    }

    public CatalogoProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(CatalogoProductoEntity producto) {
        this.producto = producto;
    }

    public TurnoEntity getTurno() {
        return turno;
    }

    public void setTurno(TurnoEntity turno) {
        this.turno = turno;
    }

    public Integer getNumBachesPlan() {
        return numBachesPlan;
    }

    public void setNumBachesPlan(Integer numBachesPlan) {
        this.numBachesPlan = numBachesPlan;
    }

    public BigDecimal getKgBachePlan() {
        return kgBachePlan;
    }

    public void setKgBachePlan(BigDecimal kgBachePlan) {
        this.kgBachePlan = kgBachePlan;
    }

    public FormulaVersionEntity getFormulaVersion() {
        return formulaVersion;
    }

    public void setFormulaVersion(FormulaVersionEntity formulaVersion) {
        this.formulaVersion = formulaVersion;
    }

    public UsuarioEntity getJefeProduccion() {
        return jefeProduccion;
    }

    public void setJefeProduccion(UsuarioEntity jefeProduccion) {
        this.jefeProduccion = jefeProduccion;
    }

    public EstadoProgramacionProduccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoProgramacionProduccion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}