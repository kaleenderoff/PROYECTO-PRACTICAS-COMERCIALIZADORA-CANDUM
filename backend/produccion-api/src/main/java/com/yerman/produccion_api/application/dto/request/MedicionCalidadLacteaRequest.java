package com.yerman.produccion_api.application.dto.request;

import com.yerman.produccion_api.domain.model.TipoMedicionCalidadLactea;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MedicionCalidadLacteaRequest {

    @NotNull
    private Long idProduccionLactea;

    private Long idProduccionLacteaBatch;

    @NotNull
    private TipoMedicionCalidadLactea tipoMedicion;

    @NotBlank
    @Size(max = 80)
    private String referencia;

    @DecimalMin(value = "0.00")
    private BigDecimal brix;

    @DecimalMin(value = "0.00")
    private BigDecimal ph;

    private LocalDateTime fechaHoraMedicion;

    @NotNull
    private Long idUsuarioCalidad;

    @Size(max = 500)
    private String observaciones;

    public Long getIdProduccionLactea() {
        return idProduccionLactea;
    }

    public void setIdProduccionLactea(Long idProduccionLactea) {
        this.idProduccionLactea = idProduccionLactea;
    }

    public Long getIdProduccionLacteaBatch() {
        return idProduccionLacteaBatch;
    }

    public void setIdProduccionLacteaBatch(Long idProduccionLacteaBatch) {
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
    }

    public TipoMedicionCalidadLactea getTipoMedicion() {
        return tipoMedicion;
    }

    public void setTipoMedicion(TipoMedicionCalidadLactea tipoMedicion) {
        this.tipoMedicion = tipoMedicion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getBrix() {
        return brix;
    }

    public void setBrix(BigDecimal brix) {
        this.brix = brix;
    }

    public BigDecimal getPh() {
        return ph;
    }

    public void setPh(BigDecimal ph) {
        this.ph = ph;
    }

    public LocalDateTime getFechaHoraMedicion() {
        return fechaHoraMedicion;
    }

    public void setFechaHoraMedicion(LocalDateTime fechaHoraMedicion) {
        this.fechaHoraMedicion = fechaHoraMedicion;
    }

    public Long getIdUsuarioCalidad() {
        return idUsuarioCalidad;
    }

    public void setIdUsuarioCalidad(Long idUsuarioCalidad) {
        this.idUsuarioCalidad = idUsuarioCalidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
