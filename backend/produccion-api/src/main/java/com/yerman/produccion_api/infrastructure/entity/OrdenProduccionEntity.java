package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "fecha_inicio_real")
    private LocalDateTime fechaInicioReal;

    @Column(name = "fecha_fin_real")
    private LocalDateTime fechaFinReal;

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
}