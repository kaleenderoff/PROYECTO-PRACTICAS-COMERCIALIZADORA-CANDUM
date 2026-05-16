package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EjecucionBatch {
    private Long id;
    private Long idOrdenProduccion;
    private Integer numeroBatch;
    private Long idMarmita;
    private String nombreMarmita;
    private BigDecimal kgEntrada;
    private BigDecimal kgProducidos;
    private BigDecimal rendimientoPct;
    private EstadoBatch estado;
    private String observaciones;
    private Boolean huboReproceso;
    private Boolean batchConforme;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public enum EstadoBatch {
        EN_PROCESO, FINALIZADO, CON_NOVEDAD
    }

    public EjecucionBatch() {}

    public EjecucionBatch(Long id, Long idOrdenProduccion, Integer numeroBatch, Long idMarmita, 
                         BigDecimal kgEntrada, BigDecimal kgProducidos, BigDecimal rendimientoPct, 
                         EstadoBatch estado, String observaciones, Boolean huboReproceso, Boolean batchConforme,
                         LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.idOrdenProduccion = idOrdenProduccion;
        this.numeroBatch = numeroBatch;
        this.idMarmita = idMarmita;
        this.kgEntrada = kgEntrada;
        this.kgProducidos = kgProducidos;
        this.rendimientoPct = rendimientoPct;
        this.estado = estado;
        this.observaciones = observaciones;
        this.huboReproceso = huboReproceso;
        this.batchConforme = batchConforme;
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

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
}
