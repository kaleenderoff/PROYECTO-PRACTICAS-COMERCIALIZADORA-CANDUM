package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El gramaje del producto es obligatorio")
    @DecimalMin(value = "0.01", message = "El gramaje del producto debe ser mayor que cero")
    private BigDecimal gramajeG;

    private String marca;

    @NotBlank(message = "La unidad de medida del producto es obligatoria")
    private String unidadMedida;

    private Boolean activo;

    @NotNull(message = "El id de la línea de producción es obligatorio")
    private Long idLineaProduccion;

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
}
