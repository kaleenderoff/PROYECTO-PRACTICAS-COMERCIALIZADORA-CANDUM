package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LineaProduccionRequest {

    @NotBlank(message = "El nombre de la línea de producción es obligatorio")
    private String nombre;

    private String descripcion;

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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
