package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ValidacionRequest {

    @NotNull(message = "El id del detalle de producción es obligatorio")
    private Long idDetalleProduccion;

    @NotNull(message = "El id del validador es obligatorio")
    private Long idValidador;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres")
    private String observacion;

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public Long getIdValidador() {
        return idValidador;
    }

    public void setIdValidador(Long idValidador) {
        this.idValidador = idValidador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}