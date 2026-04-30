package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recepcion_leche")
public class RecepcionLecheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDate fechaRecepcion;

    @Column(name = "tipo_materia_prima", nullable = false)
    private String tipoMateriaPrima;

    @Column(name = "proveedor", nullable = false)
    private String proveedor;

    @Column(name = "cantidad_recibida_litros", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadRecibidaLitros;

    @Column(name = "recibido_por")
    private String recibidoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tanque", nullable = false)
    private TanqueLecheEntity tanque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimiento_leche")
    private MovimientoLecheEntity movimientoLeche;

    @Column(name = "numero_remision")
    private String numeroRemision;

    @Column(name = "cantidad_remision_litros", precision = 14, scale = 3)
    private BigDecimal cantidadRemisionLitros;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "recepcionLeche", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecepcionLechePesajeEntity> pesajes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public RecepcionLecheEntity() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public String getTipoMateriaPrima() {
        return tipoMateriaPrima;
    }

    public String getProveedor() {
        return proveedor;
    }

    public BigDecimal getCantidadRecibidaLitros() {
        return cantidadRecibidaLitros;
    }

    public String getRecibidoPor() {
        return recibidoPor;
    }

    public TanqueLecheEntity getTanque() {
        return tanque;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public MovimientoLecheEntity getMovimientoLeche() {
        return movimientoLeche;
    }

    public String getNumeroRemision() {
        return numeroRemision;
    }

    public BigDecimal getCantidadRemisionLitros() {
        return cantidadRemisionLitros;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<RecepcionLechePesajeEntity> getPesajes() {
        return pesajes;
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

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public void setTipoMateriaPrima(String tipoMateriaPrima) {
        this.tipoMateriaPrima = tipoMateriaPrima;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setCantidadRecibidaLitros(BigDecimal cantidadRecibidaLitros) {
        this.cantidadRecibidaLitros = cantidadRecibidaLitros;
    }

    public void setRecibidoPor(String recibidoPor) {
        this.recibidoPor = recibidoPor;
    }

    public void setTanque(TanqueLecheEntity tanque) {
        this.tanque = tanque;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public void setMovimientoLeche(MovimientoLecheEntity movimientoLeche) {
        this.movimientoLeche = movimientoLeche;
    }

    public void setNumeroRemision(String numeroRemision) {
        this.numeroRemision = numeroRemision;
    }

    public void setCantidadRemisionLitros(BigDecimal cantidadRemisionLitros) {
        this.cantidadRemisionLitros = cantidadRemisionLitros;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setPesajes(List<RecepcionLechePesajeEntity> pesajes) {
        this.pesajes = pesajes;
    }
}