package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produccion", uniqueConstraints = {
        @UniqueConstraint(name = "uq_produccion_numero_lote", columnNames = { "numero_lote" })
})
public class ProduccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produccion")
    private Long idProduccion;

    @Column(name = "fecha_produccion", nullable = false)
    private LocalDate fechaProduccion;

    @Column(name = "tipo_turno", nullable = false, length = 50)
    private String tipoTurno;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "observaciones_generales", length = 500)
    private String observacionesGenerales;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_linea_produccion", nullable = false)
    private LineaProduccionEntity lineaProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operario", nullable = false)
    private UsuarioEntity operario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefe_linea", nullable = false)
    private UsuarioEntity jefeLinea;

    @OneToMany(mappedBy = "produccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<DetalleProduccionEntity> detallesProduccion = new ArrayList<>();

    @OneToMany(mappedBy = "produccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ConsumoInsumoEntity> consumosInsumo = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Long idProduccion) {
        this.idProduccion = idProduccion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LineaProduccionEntity getLineaProduccion() {
        return lineaProduccion;
    }

    public void setLineaProduccion(LineaProduccionEntity lineaProduccion) {
        this.lineaProduccion = lineaProduccion;
    }

    public UsuarioEntity getOperario() {
        return operario;
    }

    public void setOperario(UsuarioEntity operario) {
        this.operario = operario;
    }

    public UsuarioEntity getJefeLinea() {
        return jefeLinea;
    }

    public void setJefeLinea(UsuarioEntity jefeLinea) {
        this.jefeLinea = jefeLinea;
    }

    public List<DetalleProduccionEntity> getDetallesProduccion() {
        return detallesProduccion;
    }

    public void setDetallesProduccion(List<DetalleProduccionEntity> detallesProduccion) {
        this.detallesProduccion = detallesProduccion;
    }

    public List<ConsumoInsumoEntity> getConsumosInsumo() {
        return consumosInsumo;
    }

    public void setConsumosInsumo(List<ConsumoInsumoEntity> consumosInsumo) {
        this.consumosInsumo = consumosInsumo;
    }
}
