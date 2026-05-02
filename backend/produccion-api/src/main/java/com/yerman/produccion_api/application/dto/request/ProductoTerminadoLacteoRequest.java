package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductoTerminadoLacteoRequest {

    @NotNull
    private Long idProduccionLacteaBatch;

    @NotBlank
    @Size(max = 120)
    private String producto;

    @Size(max = 80)
    private String lote;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal kilosProducidos;

    @DecimalMin(value = "0.000")
    private BigDecimal kilosDisponibles;

    @Size(max = 500)
    private String observaciones;

    public Long getIdProduccionLacteaBatch() {
        return idProduccionLacteaBatch;
    }

    public void setIdProduccionLacteaBatch(Long idProduccionLacteaBatch) {
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public BigDecimal getKilosDisponibles() {
        return kilosDisponibles;
    }

    public void setKilosDisponibles(BigDecimal kilosDisponibles) {
        this.kilosDisponibles = kilosDisponibles;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}