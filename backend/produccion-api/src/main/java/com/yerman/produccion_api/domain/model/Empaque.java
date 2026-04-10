package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Empaque {

    private Long id;
    private DetalleProduccion detalleProduccion;
    private ProductoTerminado productoTerminado;
    private String loteEmpaque;
    private LocalDateTime fechaEmpaque;
    private LocalDate fechaVencimiento;
    private EstadoEmpaque estado;
    private Integer cantidadUnidades;
    private Integer cantidadCajas;
    private BigDecimal pesoTotalKg;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Empaque() {
    }

    public Long getId() {
        return id;
    }

    public DetalleProduccion getDetalleProduccion() {
        return detalleProduccion;
    }

    public ProductoTerminado getProductoTerminado() {
        return productoTerminado;
    }

    public String getLoteEmpaque() {
        return loteEmpaque;
    }

    public LocalDateTime getFechaEmpaque() {
        return fechaEmpaque;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public EstadoEmpaque getEstado() {
        return estado;
    }

    public Integer getCantidadUnidades() {
        return cantidadUnidades;
    }

    public Integer getCantidadCajas() {
        return cantidadCajas;
    }

    public BigDecimal getPesoTotalKg() {
        return pesoTotalKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDetalleProduccion(DetalleProduccion detalleProduccion) {
        this.detalleProduccion = detalleProduccion;
    }

    public void setProductoTerminado(ProductoTerminado productoTerminado) {
        this.productoTerminado = productoTerminado;
    }

    public void setLoteEmpaque(String loteEmpaque) {
        this.loteEmpaque = loteEmpaque;
    }

    public void setFechaEmpaque(LocalDateTime fechaEmpaque) {
        this.fechaEmpaque = fechaEmpaque;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setEstado(EstadoEmpaque estado) {
        this.estado = estado;
    }

    public void setCantidadUnidades(Integer cantidadUnidades) {
        this.cantidadUnidades = cantidadUnidades;
    }

    public void setCantidadCajas(Integer cantidadCajas) {
        this.cantidadCajas = cantidadCajas;
    }

    public void setPesoTotalKg(BigDecimal pesoTotalKg) {
        this.pesoTotalKg = pesoTotalKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}