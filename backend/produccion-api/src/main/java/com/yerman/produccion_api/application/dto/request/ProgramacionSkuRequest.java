package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProgramacionSkuRequest {

    private Long idProgramacion;

    @NotNull(message = "El SKU es obligatorio")
    @Positive(message = "El id del SKU debe ser mayor que cero")
    private Long idSku;

    @NotNull(message = "Las unidades objetivo son obligatorias")
    @Positive(message = "Las unidades objetivo deben ser mayores que cero")
    private Integer unidadesObjetivo;

    @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres")
    private String observaciones;

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public void setIdProgramacion(Long idProgramacion) {
        this.idProgramacion = idProgramacion;
    }

    public Long getIdSku() {
        return idSku;
    }

    public void setIdSku(Long idSku) {
        this.idSku = idSku;
    }

    public Integer getUnidadesObjetivo() {
        return unidadesObjetivo;
    }

    public void setUnidadesObjetivo(Integer unidadesObjetivo) {
        this.unidadesObjetivo = unidadesObjetivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}