package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public class InsumoRequest {

    @NotBlank(message = "El nombre del insumo es obligatorio")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "La unidad de medida del insumo es obligatoria")
    private String unidadMedida;

    private Boolean activo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
