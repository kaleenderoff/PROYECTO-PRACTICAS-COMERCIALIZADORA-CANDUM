package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EjecucionBatch {
    private Long id;
    private Long idOrdenProduccion;
    private Integer numeroBatch;
    private Long idMarmita;
    private String nombreMarmita;
    private Long idMovimientoLeche;
    private BigDecimal kgEntrada;
    private BigDecimal kgProducidos;
    private BigDecimal rendimientoPct;
    private EstadoBatch estado;
    private String observaciones;
    private Boolean huboReproceso;
    private Boolean batchConforme;
    private BigDecimal brixFinal;
    private String tipoNovedad;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public enum EstadoBatch {
        EN_PROCESO, FINALIZADO, CON_NOVEDAD
    }

    public enum TipoNovedad {
        BAJA_GRASA, FALLA_CALDERA, RETRASO_LECHE, FALLA_EQUIPO,
        BRIX_FUERA_RANGO, REPROCESO, CAMBIO_PROCESO, OTRO
    }

    public EjecucionBatch() {}

    public EjecucionBatch(Long id, Long idOrdenProduccion, Integer numeroBatch, Long idMarmita, Long idMovimientoLeche,
                         BigDecimal kgEntrada, BigDecimal kgProducidos, BigDecimal rendimientoPct,
                         EstadoBatch estado, String observaciones, Boolean huboReproceso, Boolean batchConforme,
                         BigDecimal brixFinal, String tipoNovedad, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.idOrdenProduccion = idOrdenProduccion;
        this.numeroBatch = numeroBatch;
        this.idMarmita = idMarmita;
        this.idMovimientoLeche = idMovimientoLeche;
        this.kgEntrada = kgEntrada;
        this.kgProducidos = kgProducidos;
        this.rendimientoPct = rendimientoPct;
        this.estado = estado;
        this.observaciones = observaciones;
        this.huboReproceso = huboReproceso;
        this.batchConforme = batchConforme;
        this.brixFinal = brixFinal;
        this.tipoNovedad = tipoNovedad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdOrdenProduccion() { return idOrdenProduccion; }
    public void setIdOrdenProduccion(Long idOrdenProduccion) { this.idOrdenProduccion = idOrdenProduccion; }

    public Integer getNumeroBatch() { return numeroBatch; }
    public void setNumeroBatch(Integer numeroBatch) { this.numeroBatch = numeroBatch; }

    public Long getIdMarmita() { return idMarmita; }
    public void setIdMarmita(Long idMarmita) { this.idMarmita = idMarmita; }

    public String getNombreMarmita() { return nombreMarmita; }
    public void setNombreMarmita(String nombreMarmita) { this.nombreMarmita = nombreMarmita; }

    public Long getIdMovimientoLeche() { return idMovimientoLeche; }
    public void setIdMovimientoLeche(Long idMovimientoLeche) { this.idMovimientoLeche = idMovimientoLeche; }

    public BigDecimal getKgEntrada() { return kgEntrada; }
    public void setKgEntrada(BigDecimal kgEntrada) { this.kgEntrada = kgEntrada; }

    public BigDecimal getKgProducidos() { return kgProducidos; }
    public void setKgProducidos(BigDecimal kgProducidos) { this.kgProducidos = kgProducidos; }

    public BigDecimal getRendimientoPct() { return rendimientoPct; }
    public void setRendimientoPct(BigDecimal rendimientoPct) { this.rendimientoPct = rendimientoPct; }

    public EstadoBatch getEstado() { return estado; }
    public void setEstado(EstadoBatch estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Boolean getHuboReproceso() { return huboReproceso; }
    public void setHuboReproceso(Boolean huboReproceso) { this.huboReproceso = huboReproceso; }

    public Boolean getBatchConforme() { return batchConforme; }
    public void setBatchConforme(Boolean batchConforme) { this.batchConforme = batchConforme; }

    public BigDecimal getBrixFinal() { return brixFinal; }
    public void setBrixFinal(BigDecimal brixFinal) { this.brixFinal = brixFinal; }

    public String getTipoNovedad() { return tipoNovedad; }
    public void setTipoNovedad(String tipoNovedad) { this.tipoNovedad = tipoNovedad; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
}
