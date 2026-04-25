package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class IniciarOrdenProduccionRequest {

    @NotNull(message = "El id del jefe de línea ejecutor es obligatorio")
    private Long idJefeLineaEjecutor;

    public Long getIdJefeLineaEjecutor() {
        return idJefeLineaEjecutor;
    }

    public void setIdJefeLineaEjecutor(Long idJefeLineaEjecutor) {
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
    }
}