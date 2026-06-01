package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CerrarTandasRequest {

    @NotNull(message = "El usuario que cierra las tandas es obligatorio.")
    private Long idUsuarioCierre;

    @Size(max = 1000, message = "Las observaciones no pueden superar 1000 caracteres.")
    private String observaciones;

    public CerrarTandasRequest() {
    }

    public Long getIdUsuarioCierre() {
        return idUsuarioCierre;
    }

    public void setIdUsuarioCierre(Long idUsuarioCierre) {
        this.idUsuarioCierre = idUsuarioCierre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}