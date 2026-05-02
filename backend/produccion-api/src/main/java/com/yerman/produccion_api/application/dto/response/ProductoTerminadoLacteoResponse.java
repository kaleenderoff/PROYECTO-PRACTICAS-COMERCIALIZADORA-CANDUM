package com.yerman.produccion_api.application.dto.response;

import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;

import java.math.BigDecimal;

public class ProductoTerminadoLacteoResponse {

    private Long id;
    private Long idProduccionLacteaBatch;
    private String producto;
    private String lote;
    private BigDecimal kilosProducidos;
    private BigDecimal kilosDisponibles;
    private EstadoProductoTerminadoLacteo estado;
    private String observaciones;

    public ProductoTerminadoLacteoResponse(Long id, Long idProduccionLacteaBatch,
            String producto, String lote, BigDecimal kilosProducidos,
            BigDecimal kilosDisponibles, EstadoProductoTerminadoLacteo estado,
            String observaciones) {
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
}