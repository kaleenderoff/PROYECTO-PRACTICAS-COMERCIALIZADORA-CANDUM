package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class OrdenProduccionDetalle {

    private Long id;
    private Long idOrden;
    private Long idProgramacionSku;
    private Long idSku;
    private BigDecimal cantidadProgramada;
    private String unidadProgramada;
    private Integer prioridad;
    private String observaciones;

    public OrdenProduccionDetalle() {
    }

    public OrdenProduccionDetalle(Long id, Long idOrden, Long idProgramacionSku, Long idSku,
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public void setIdProgramacionSku(Long idProgramacionSku) {
        this.idProgramacionSku = idProgramacionSku;
    }

    public void setIdSku(Long idSku) {
        this.idSku = idSku;
    }

    public void setCantidadProgramada(BigDecimal cantidadProgramada) {
        this.cantidadProgramada = cantidadProgramada;
    }

    public void setUnidadProgramada(String unidadProgramada) {
        this.unidadProgramada = unidadProgramada;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}