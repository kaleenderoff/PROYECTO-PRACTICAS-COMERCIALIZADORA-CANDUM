package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class ProductoTerminadoLacteo {

    private Long id;
    private Long idProduccionLacteaBatch;
    private String producto;
    private String lote;
    private BigDecimal kilosProducidos;
    private BigDecimal kilosDisponibles;
    private EstadoProductoTerminadoLacteo estado;
    private String observaciones;

    public ProductoTerminadoLacteo() {
    }

    public ProductoTerminadoLacteo(Long id, Long idProduccionLacteaBatch, String producto, String lote,
            BigDecimal kilosProducidos, BigDecimal kilosDisponibles,
            EstadoProductoTerminadoLacteo estado, String observaciones) {
        this.id = id;
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
        this.producto = producto;
        this.lote = lote;
        this.kilosProducidos = kilosProducidos;
        this.kilosDisponibles = kilosDisponibles;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduccionLacteaBatch() {
        return idProduccionLacteaBatch;
    }

    public String getProducto() {
        return producto;
    }

    public String getLote() {
        return lote;
    }

    public BigDecimal getKilosProducidos() {
        return kilosProducidos;
    }

    public BigDecimal getKilosDisponibles() {
        return kilosDisponibles;
    }

    public EstadoProductoTerminadoLacteo getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProduccionLacteaBatch(Long idProduccionLacteaBatch) {
        this.idProduccionLacteaBatch = idProduccionLacteaBatch;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public void setKilosProducidos(BigDecimal kilosProducidos) {
        this.kilosProducidos = kilosProducidos;
    }

    public void setKilosDisponibles(BigDecimal kilosDisponibles) {
        this.kilosDisponibles = kilosDisponibles;
    }

    public void setEstado(EstadoProductoTerminadoLacteo estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}