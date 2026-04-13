package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DashboardTrazabilidadEmpaqueResponse {

    private Long idEmpaque;
    private Long idProductoTerminado;
    private String sku;
    private String nombreComercial;
    private String loteEmpaque;
    private LocalDateTime fechaEmpaque;
    private LocalDate fechaVencimiento;
    private String estadoEmpaque;
    private Integer cantidadUnidades;
    private Integer cantidadCajas;

    public DashboardTrazabilidadEmpaqueResponse() {
    }

    public Long getIdEmpaque() {
        return idEmpaque;
    }

    public void setIdEmpaque(Long idEmpaque) {
        this.idEmpaque = idEmpaque;
    }

    public Long getIdProductoTerminado() {
        return idProductoTerminado;
    }

    public void setIdProductoTerminado(Long idProductoTerminado) {
        this.idProductoTerminado = idProductoTerminado;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
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

    public String getEstadoEmpaque() {
        return estadoEmpaque;
    }

    public void setEstadoEmpaque(String estadoEmpaque) {
        this.estadoEmpaque = estadoEmpaque;
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
}