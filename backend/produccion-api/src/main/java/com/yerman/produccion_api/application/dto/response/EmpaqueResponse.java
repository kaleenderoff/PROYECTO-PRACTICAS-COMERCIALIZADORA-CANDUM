package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmpaqueResponse {

    private Long id;
    private Long idDetalleProduccion;
    private Long idProductoTerminado;
    private String skuProductoTerminado;
    private String nombreComercialProductoTerminado;
    private String loteEmpaque;
    private LocalDateTime fechaEmpaque;
    private LocalDate fechaVencimiento;
    private String estado;
    private Integer cantidadUnidades;
    private Integer cantidadCajas;
    private BigDecimal pesoTotalKg;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EmpaqueResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public Long getIdProductoTerminado() {
        return idProductoTerminado;
    }

    public void setIdProductoTerminado(Long idProductoTerminado) {
        this.idProductoTerminado = idProductoTerminado;
    }

    public String getSkuProductoTerminado() {
        return skuProductoTerminado;
    }

    public void setSkuProductoTerminado(String skuProductoTerminado) {
        this.skuProductoTerminado = skuProductoTerminado;
    }

    public String getNombreComercialProductoTerminado() {
        return nombreComercialProductoTerminado;
    }

    public void setNombreComercialProductoTerminado(String nombreComercialProductoTerminado) {
        this.nombreComercialProductoTerminado = nombreComercialProductoTerminado;
    }

    public String getLoteEmpaque() {
        return loteEmpaque;
    }

    public void setLoteEmpaque(String loteEmpaque) {
        this.loteEmpaque = loteEmpaque;
    }

    public LocalDateTime getFechaEmpaque() {
        return fechaEmpaque;
    }

    public void setFechaEmpaque(LocalDateTime fechaEmpaque) {
        this.fechaEmpaque = fechaEmpaque;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCantidadUnidades() {
        return cantidadUnidades;
    }

    public void setCantidadUnidades(Integer cantidadUnidades) {
        this.cantidadUnidades = cantidadUnidades;
    }

    public Integer getCantidadCajas() {
        return cantidadCajas;
    }

    public void setCantidadCajas(Integer cantidadCajas) {
        this.cantidadCajas = cantidadCajas;
    }

    public BigDecimal getPesoTotalKg() {
        return pesoTotalKg;
    }

    public void setPesoTotalKg(BigDecimal pesoTotalKg) {
        this.pesoTotalKg = pesoTotalKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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