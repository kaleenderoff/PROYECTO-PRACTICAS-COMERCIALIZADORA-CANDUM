package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public class ProduccionLacteaResponse {

    private Long id;
    private Long idOrdenProduccion;
    private LocalDate fechaProduccion;
    private String producto;
    private Long idTanque;
    private Long idUsuario;
    private String observaciones;
    private List<ProduccionLacteaBatchResponse> batches;

    public ProduccionLacteaResponse(Long id, Long idOrdenProduccion, LocalDate fechaProduccion, String producto,
            Long idTanque, Long idUsuario, String observaciones,
            List<ProduccionLacteaBatchResponse> batches) {
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

    public List<ProduccionLacteaBatchResponse> getBatches() {
        return batches;
    }
}
