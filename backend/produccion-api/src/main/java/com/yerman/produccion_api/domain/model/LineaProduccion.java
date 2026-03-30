package com.yerman.produccion_api.domain.model;

import java.time.LocalDateTime;

public class LineaProduccion {

    private Long idLineaProduccion;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LineaProduccion() {
    }

    public LineaProduccion(Long idLineaProduccion, String nombre, String descripcion, Boolean activo,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idLineaProduccion = idLineaProduccion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getIdLineaProduccion() {
        return idLineaProduccion;
    }

    public void setIdLineaProduccion(Long idLineaProduccion) {
        this.idLineaProduccion = idLineaProduccion;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
