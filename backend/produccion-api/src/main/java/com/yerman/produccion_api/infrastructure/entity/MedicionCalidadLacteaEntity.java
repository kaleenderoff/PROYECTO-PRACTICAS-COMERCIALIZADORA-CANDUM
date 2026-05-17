package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.TipoMedicionCalidadLactea;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicion_calidad_lactea")
public class MedicionCalidadLacteaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion_lactea")
    private ProduccionLacteaEntity produccionLactea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produccion_lactea_batch")
    private ProduccionLacteaBatchEntity produccionLacteaBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_produccion")
    private OrdenProduccionEntity ordenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejecucion_batch")
    private EjecucionBatchEntity ejecucionBatch;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_medicion", nullable = false, length = 20)
    private TipoMedicionCalidadLactea tipoMedicion;

    @Column(name = "referencia", nullable = false, length = 80)
    private String referencia;

    @Column(name = "brix", precision = 5, scale = 2)
    private BigDecimal brix;

    @Column(name = "ph", precision = 4, scale = 2)
    private BigDecimal ph;

    @Column(name = "fecha_hora_medicion", nullable = false)
    private LocalDateTime fechaHoraMedicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_calidad", nullable = false)
    private UsuarioEntity usuarioCalidad;

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

    public OrdenProduccionEntity getOrdenProduccion() {
        return ordenProduccion;
    }

    public EjecucionBatchEntity getEjecucionBatch() {
        return ejecucionBatch;
    }

    public TipoMedicionCalidadLactea getTipoMedicion() {
        return tipoMedicion;
    }

    public String getReferencia() {
        return referencia;
    }

    public BigDecimal getBrix() {
        return brix;
    }

    public BigDecimal getPh() {
        return ph;
    }

    public LocalDateTime getFechaHoraMedicion() {
        return fechaHoraMedicion;
    }

    public UsuarioEntity getUsuarioCalidad() {
        return usuarioCalidad;
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

    public void setOrdenProduccion(OrdenProduccionEntity ordenProduccion) {
        this.ordenProduccion = ordenProduccion;
    }

    public void setEjecucionBatch(EjecucionBatchEntity ejecucionBatch) {
        this.ejecucionBatch = ejecucionBatch;
    }

    public void setTipoMedicion(TipoMedicionCalidadLactea tipoMedicion) {
        this.tipoMedicion = tipoMedicion;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setBrix(BigDecimal brix) {
        this.brix = brix;
    }

    public void setPh(BigDecimal ph) {
        this.ph = ph;
    }

    public void setFechaHoraMedicion(LocalDateTime fechaHoraMedicion) {
        this.fechaHoraMedicion = fechaHoraMedicion;
    }

    public void setUsuarioCalidad(UsuarioEntity usuarioCalidad) {
        this.usuarioCalidad = usuarioCalidad;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
