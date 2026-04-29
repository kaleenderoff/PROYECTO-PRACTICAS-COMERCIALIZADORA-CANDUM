package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_leche")
public class MovimientoLecheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tanque", nullable = false)
    private TanqueLecheEntity tanque;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimientoLeche tipoMovimiento;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "cantidad_litros", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadLitros;

    @Column(name = "saldo_resultante_litros", nullable = false, precision = 14, scale = 3)
    private BigDecimal saldoResultanteLitros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public MovimientoLecheEntity() {
    }

    public Long getId() {
        return id;
    }

    public TanqueLecheEntity getTanque() {
        return tanque;
    }

    public void setTanque(TanqueLecheEntity tanque) {
        this.tanque = tanque;
    }

    public TipoMovimientoLeche getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimientoLeche tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public BigDecimal getCantidadLitros() {
        return cantidadLitros;
    }

    public void setCantidadLitros(BigDecimal cantidadLitros) {
        this.cantidadLitros = cantidadLitros;
    }

    public BigDecimal getSaldoResultanteLitros() {
        return saldoResultanteLitros;
    }

    public void setSaldoResultanteLitros(BigDecimal saldoResultanteLitros) {
        this.saldoResultanteLitros = saldoResultanteLitros;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
}