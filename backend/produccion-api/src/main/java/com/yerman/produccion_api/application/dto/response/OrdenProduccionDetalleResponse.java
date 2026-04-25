package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class OrdenProduccionDetalleResponse {

    private Long id;
    private Long idOrden;
    private Long idProgramacionSku;
    private Long idSku;
    private BigDecimal cantidadProgramada;
    private String unidadProgramada;
    private Integer prioridad;
    private String observaciones;

    public OrdenProduccionDetalleResponse(Long id, Long idOrden, Long idProgramacionSku, Long idSku,
            BigDecimal cantidadProgramada, String unidadProgramada, Integer prioridad, String observaciones) {
        this.id = id;
        this.idOrden = idOrden;
        this.idProgramacionSku = idProgramacionSku;
        this.idSku = idSku;
        this.cantidadProgramada = cantidadProgramada;
        this.unidadProgramada = unidadProgramada;
        this.prioridad = prioridad;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public Long getIdProgramacionSku() {
        return idProgramacionSku;
    }

    public Long getIdSku() {
        return idSku;
    }

    public BigDecimal getCantidadProgramada() {
        return cantidadProgramada;
    }

    public String getUnidadProgramada() {
        return unidadProgramada;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public String getObservaciones() {
        return observaciones;
    }
}