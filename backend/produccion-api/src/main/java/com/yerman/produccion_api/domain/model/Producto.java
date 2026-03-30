package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {

    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal gramajeG;
    private String marca;
    private String unidadMedida;
    private Boolean activo;
    private LineaProduccion lineaProduccion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Producto() {
    }

    public Producto(Long idProducto, String nombre, String descripcion, BigDecimal gramajeG, String marca,
            String unidadMedida, Boolean activo, LineaProduccion lineaProduccion,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.gramajeG = gramajeG;
        this.marca = marca;
        this.unidadMedida = unidadMedida;
        this.activo = activo;
        this.lineaProduccion = lineaProduccion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public LineaProduccion getLineaProduccion() {
        return lineaProduccion;
    }

    public void setLineaProduccion(LineaProduccion lineaProduccion) {
        this.lineaProduccion = lineaProduccion;
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
