package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoResponse {

    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal gramajeG;
    private String marca;
    private String unidadMedida;
    private Boolean activo;
    private Long idLineaProduccion;
    private String nombreLineaProduccion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
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

    public BigDecimal getGramajeG() {
        return gramajeG;
    }

    public void setGramajeG(BigDecimal gramajeG) {
        this.gramajeG = gramajeG;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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

    public Long getIdLineaProduccion() {
        return idLineaProduccion;
    }

    public void setIdLineaProduccion(Long idLineaProduccion) {
        this.idLineaProduccion = idLineaProduccion;
    }

    public String getNombreLineaProduccion() {
        return nombreLineaProduccion;
    }

    public void setNombreLineaProduccion(String nombreLineaProduccion) {
        this.nombreLineaProduccion = nombreLineaProduccion;
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
