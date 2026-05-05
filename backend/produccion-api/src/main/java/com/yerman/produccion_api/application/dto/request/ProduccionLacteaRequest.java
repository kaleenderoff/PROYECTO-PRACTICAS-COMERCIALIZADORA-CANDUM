package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class ProduccionLacteaRequest {

    @NotNull
    private LocalDate fechaProduccion;

    private Long idOrdenProduccion;

    @NotBlank
    @Size(max = 120)
    private String producto;

    @NotNull
    private Long idTanque;

    @NotNull
    private Long idUsuario;

    @Size(max = 500)
    private String observaciones;

    @Valid
    @NotNull
    private List<ProduccionLacteaBatchRequest> batches;

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public Long getIdOrdenProduccion() {
        return idOrdenProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setIdOrdenProduccion(Long idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public void setIdTanque(Long idTanque) {
        this.idTanque = idTanque;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ProduccionLacteaBatchRequest> getBatches() {
        return batches;
    }

    public void setBatches(List<ProduccionLacteaBatchRequest> batches) {
        this.batches = batches;
    }
}
