package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrdenProduccionRequest {

    @NotNull(message = "El id de la programación es obligatorio")
    private Long idProgramacion;

    @NotNull(message = "El id del usuario creador es obligatorio")
    private Long idCreadaPor;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public void setIdProgramacion(Long idProgramacion) {
        this.idProgramacion = idProgramacion;
    }

    public Long getIdCreadaPor() {
        return idCreadaPor;
    }

    public void setIdCreadaPor(Long idCreadaPor) {
        this.idCreadaPor = idCreadaPor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}