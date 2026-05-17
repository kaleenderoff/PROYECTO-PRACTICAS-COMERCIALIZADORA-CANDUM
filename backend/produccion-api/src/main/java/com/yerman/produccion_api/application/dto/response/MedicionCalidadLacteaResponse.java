package com.yerman.produccion_api.application.dto.response;

import com.yerman.produccion_api.domain.model.TipoMedicionCalidadLactea;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MedicionCalidadLacteaResponse {

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

    public MedicionCalidadLacteaResponse(Long id, Long idProduccionLactea, Long idProduccionLacteaBatch,
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
}
