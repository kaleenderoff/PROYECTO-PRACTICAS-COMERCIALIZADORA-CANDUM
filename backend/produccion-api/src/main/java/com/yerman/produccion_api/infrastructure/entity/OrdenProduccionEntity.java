package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_produccion", uniqueConstraints = {
        @UniqueConstraint(name = "uq_orden_numero", columnNames = "numero_orden"),
        @UniqueConstraint(name = "uq_orden_programacion", columnNames = "id_programacion")
})
public class OrdenProduccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orden", nullable = false, length = 40)
    private String numeroOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programacion", nullable = false)
    private ProgramacionProduccionEntity programacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_linea", nullable = false)
    private CatalogoLineaEntity linea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private CatalogoProductoEntity producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turno", nullable = false)
    private TurnoEntity turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefe_linea_ejecutor")
    private UsuarioEntity jefeLineaEjecutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creada_por", nullable = false)
    private UsuarioEntity creadaPor;

    @Column(name = "fecha_produccion", nullable = false)
    private LocalDate fechaProduccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrdenProduccion estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tanque_leche")
    private TanqueLecheEntity tanqueLeche;

    @Column(name = "fecha_inicio_real")
    private LocalDateTime fechaInicioReal;

    @Column(name = "fecha_fin_real")
    private LocalDateTime fechaFinReal;

    @Column(name = "kg_entrada_real", precision = 12, scale = 2)
    private BigDecimal kgEntradaReal;

    @Column(name = "kg_producido_batches", precision = 12, scale = 2)
    private BigDecimal kgProducidoBatches;

    @Column(name = "kg_pt_real", precision = 12, scale = 2)
    private BigDecimal kgPtReal;

    @Column(name = "rendimiento_real", precision = 12, scale = 2)
    private BigDecimal rendimientoReal;

    @Column(name = "merma_real", precision = 12, scale = 2)
    private BigDecimal mermaReal;

    @Column(name = "merma_empaque", precision = 12, scale = 2)
    private BigDecimal mermaEmpaque;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenProduccionDetalleEntity> detalles = new ArrayList<>();

    public OrdenProduccionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public ProgramacionProduccionEntity getProgramacion() {
        return programacion;
    }

    public void setProgramacion(ProgramacionProduccionEntity programacion) {
        this.programacion = programacion;
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

    public UsuarioEntity getJefeLineaEjecutor() {
        return jefeLineaEjecutor;
    }

    public void setJefeLineaEjecutor(UsuarioEntity jefeLineaEjecutor) {
        this.jefeLineaEjecutor = jefeLineaEjecutor;
    }

    public UsuarioEntity getCreadaPor() {
        return creadaPor;
    }

    public void setCreadaPor(UsuarioEntity creadaPor) {
        this.creadaPor = creadaPor;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public EstadoOrdenProduccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrdenProduccion estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TanqueLecheEntity getTanqueLeche() {
        return tanqueLeche;
    }

    public void setTanqueLeche(TanqueLecheEntity tanqueLeche) {
        this.tanqueLeche = tanqueLeche;
    }

    public LocalDateTime getFechaInicioReal() {
        return fechaInicioReal;
    }

    public void setFechaInicioReal(LocalDateTime fechaInicioReal) {
        this.fechaInicioReal = fechaInicioReal;
    }

    public LocalDateTime getFechaFinReal() {
        return fechaFinReal;
    }

    public void setFechaFinReal(LocalDateTime fechaFinReal) {
        this.fechaFinReal = fechaFinReal;
    }

    public BigDecimal getKgEntradaReal() {
        return kgEntradaReal;
    }

    public void setKgEntradaReal(BigDecimal kgEntradaReal) {
        this.kgEntradaReal = kgEntradaReal;
    }

    public BigDecimal getKgProducidoBatches() {
        return kgProducidoBatches;
    }

    public void setKgProducidoBatches(BigDecimal kgProducidoBatches) {
        this.kgProducidoBatches = kgProducidoBatches;
    }

    public BigDecimal getKgPtReal() {
        return kgPtReal;
    }

    public void setKgPtReal(BigDecimal kgPtReal) {
        this.kgPtReal = kgPtReal;
    }

    public BigDecimal getRendimientoReal() {
        return rendimientoReal;
    }

    public void setRendimientoReal(BigDecimal rendimientoReal) {
        this.rendimientoReal = rendimientoReal;
    }

    public BigDecimal getMermaReal() {
        return mermaReal;
    }

    public void setMermaReal(BigDecimal mermaReal) {
        this.mermaReal = mermaReal;
    }

    public BigDecimal getMermaEmpaque() {
        return mermaEmpaque;
    }

    public void setMermaEmpaque(BigDecimal mermaEmpaque) {
        this.mermaEmpaque = mermaEmpaque;
    }

    public List<OrdenProduccionDetalleEntity> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenProduccionDetalleEntity> detalles) {
        this.detalles = detalles;
    }
}