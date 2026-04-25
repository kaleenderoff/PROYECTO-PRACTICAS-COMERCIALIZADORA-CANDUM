package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class OrdenProduccionDetalleRequest {

    private Long idProgramacionSku;

    @NotNull(message = "El id del SKU es obligatorio")
    private Long idSku;

    @NotNull(message = "La cantidad programada es obligatoria")
    @DecimalMin(value = "0.001", message = "La cantidad programada debe ser mayor que cero")
    private BigDecimal cantidadProgramada;

    @Size(max = 50, message = "La unidad programada no puede superar los 50 caracteres")
    private String unidadProgramada;

    private Integer prioridad;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    public Long getIdProgramacionSku() {
        return idProgramacionSku;
    }

    public void setIdProgramacionSku(Long idProgramacionSku) {
        this.idProgramacionSku = idProgramacionSku;
    }

    public Long getIdSku() {
        return idSku;
    }

    public void setIdSku(Long idSku) {
        this.idSku = idSku;
    }

    public BigDecimal getCantidadProgramada() {
        return cantidadProgramada;
    }

    public void setCantidadProgramada(BigDecimal cantidadProgramada) {
        this.cantidadProgramada = cantidadProgramada;
    }

    public String getUnidadProgramada() {
        return unidadProgramada;
    }

    public void setUnidadProgramada(String unidadProgramada) {
        this.unidadProgramada = unidadProgramada;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}