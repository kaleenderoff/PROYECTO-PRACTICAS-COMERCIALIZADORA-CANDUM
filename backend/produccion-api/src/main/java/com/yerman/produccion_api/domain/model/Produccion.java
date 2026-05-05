package com.yerman.produccion_api.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Produccion {

    private Long id;
    private Long idOrdenProduccion;
    private LocalDate fechaProduccion;
    private String producto;
    private Long idTanque;
    private Long idUsuario;
    private String observaciones;
    private List<ProduccionBatch> batches;

    public Produccion() {
    }

    public Produccion(Long id, Long idOrdenProduccion, LocalDate fechaProduccion, String producto,
            Long idTanque, Long idUsuario, String observaciones,
            List<ProduccionBatch> batches) {
        this.id = id;
        this.idOrdenProduccion = idOrdenProduccion;
        this.fechaProduccion = fechaProduccion;
        this.producto = producto;
        this.idTanque = idTanque;
        this.idUsuario = idUsuario;
        this.observaciones = observaciones;
        this.batches = batches;
    }

    public Long getId() {
        return id;
    }

    public Long getIdOrdenProduccion() {
        return idOrdenProduccion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public String getProducto() {
        return producto;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<ProduccionBatch> getBatches() {
        return batches;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdOrdenProduccion(Long idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setIdTanque(Long idTanque) {
        this.idTanque = idTanque;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setBatches(List<ProduccionBatch> batches) {
        this.batches = batches;
    }
}
