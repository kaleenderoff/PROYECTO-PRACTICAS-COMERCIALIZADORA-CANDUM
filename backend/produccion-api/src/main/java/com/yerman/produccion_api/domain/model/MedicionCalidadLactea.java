package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MedicionCalidadLactea {

    private Long id;
    private Long idProduccionLactea;
    private Long idProduccionLacteaBatch;
    private Long idOrdenProduccion;
    private Long idEjecucionBatch;
    private TipoMedicionCalidadLactea tipoMedicion;
    private String referencia;
    private BigDecimal brix;
    private BigDecimal ph;
    private LocalDateTime fechaHoraMedicion;
    private Long idUsuarioCalidad;
    private String observaciones;

    public MedicionCalidadLactea() {
    }

    public MedicionCalidadLactea(Long id, Long idProduccionLactea, Long idProduccionLacteaBatch,
            Long idOrdenProduccion, Long idEjecucionBatch,
            TipoMedicionCalidadLactea tipoMedicion, String referencia, BigDecimal brix, BigDecimal ph,
            LocalDateTime fechaHoraMedicion, Long idUsuarioCalidad, String observaciones) {
        this.id = id;
        this.idProduccionLactea = idProduccionLactea;
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
        this.idOrdenProduccion = idOrdenProduccion;
        this.idEjecucionBatch = idEjecucionBatch;
        this.tipoMedicion = tipoMedicion;
        this.referencia = referencia;
        this.brix = brix;
        this.ph = ph;
        this.fechaHoraMedicion = fechaHoraMedicion;
        this.idUsuarioCalidad = idUsuarioCalidad;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduccionLactea() {
        return idProduccionLactea;
    }

    public Long getIdProduccionLacteaBatch() {
        return idProduccionLacteaBatch;
    }

    public Long getIdOrdenProduccion() {
        return idOrdenProduccion;
    }

    public Long getIdEjecucionBatch() {
        return idEjecucionBatch;
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

    public Long getIdUsuarioCalidad() {
        return idUsuarioCalidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProduccionLactea(Long idProduccionLactea) {
        this.idProduccionLactea = idProduccionLactea;
    }

    public void setIdProduccionLacteaBatch(Long idProduccionLacteaBatch) {
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
    }

    public void setIdOrdenProduccion(Long idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public void setIdEjecucionBatch(Long idEjecucionBatch) {
        this.idEjecucionBatch = idEjecucionBatch;
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

    public void setIdUsuarioCalidad(Long idUsuarioCalidad) {
        this.idUsuarioCalidad = idUsuarioCalidad;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
