package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_insumo_lacteo")
public class RegistroInsumoLacteoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion_lactea", nullable = false)
    private ProduccionLacteaEntity produccionLactea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion_lactea_batch")
    private ProduccionLacteaBatchEntity produccionLacteaBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false)
    private InsumoEntity insumo;

    @Column(name = "lote_insumo", length = 100)
    private String loteInsumo;

    @Column(name = "cantidad_requerida", precision = 14, scale = 3)
    private BigDecimal cantidadRequerida;

    @Column(name = "cantidad_usada", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadUsada;

    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida;

    @Column(name = "fecha_hora_registro", nullable = false)
    private LocalDateTime fechaHoraRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public ProduccionLacteaEntity getProduccionLactea() {
        return produccionLactea;
    }

    public ProduccionLacteaBatchEntity getProduccionLacteaBatch() {
        return produccionLacteaBatch;
    }

    public InsumoEntity getInsumo() {
        return insumo;
    }

    public String getLoteInsumo() {
        return loteInsumo;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public BigDecimal getCantidadUsada() {
        return cantidadUsada;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduccionLactea(ProduccionLacteaEntity produccionLactea) {
        this.produccionLactea = produccionLactea;
    }

    public void setProduccionLacteaBatch(ProduccionLacteaBatchEntity produccionLacteaBatch) {
        this.produccionLacteaBatch = produccionLacteaBatch;
    }

    public void setInsumo(InsumoEntity insumo) {
        this.insumo = insumo;
    }

    public void setLoteInsumo(String loteInsumo) {
        this.loteInsumo = loteInsumo;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public void setCantidadUsada(BigDecimal cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
