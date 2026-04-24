package com.yerman.produccion_api.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrdenProduccion {

    private Long id;
    private String numeroOrden;
    private Long idProgramacion;
    private Long idLinea;
    private Long idProducto;
    private Long idTurno;
    private Long idJefeLineaEjecutor;
    private Long idCreadaPor;
    private LocalDate fechaProduccion;
    private EstadoOrdenProduccion estado;
    private String observaciones;
    private LocalDateTime fechaInicioReal;
    private LocalDateTime fechaFinReal;

    public OrdenProduccion() {
    }

    public OrdenProduccion(
            Long id,
            String numeroOrden,
            Long idProgramacion,
            Long idLinea,
            Long idProducto,
            Long idTurno,
            Long idJefeLineaEjecutor,
            Long idCreadaPor,
            LocalDate fechaProduccion,
            EstadoOrdenProduccion estado,
            String observaciones,
            LocalDateTime fechaInicioReal,
            LocalDateTime fechaFinReal) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.idProgramacion = idProgramacion;
        this.idLinea = idLinea;
        this.idProducto = idProducto;
        this.idTurno = idTurno;
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
        this.idCreadaPor = idCreadaPor;
        this.fechaProduccion = fechaProduccion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaInicioReal = fechaInicioReal;
        this.fechaFinReal = fechaFinReal;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public Long getIdTurno() {
        return idTurno;
    }

    public Long getIdJefeLineaEjecutor() {
        return idJefeLineaEjecutor;
    }

    public Long getIdCreadaPor() {
        return idCreadaPor;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public EstadoOrdenProduccion getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaInicioReal() {
        return fechaInicioReal;
    }

    public LocalDateTime getFechaFinReal() {
        return fechaFinReal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public void setIdProgramacion(Long idProgramacion) {
        this.idProgramacion = idProgramacion;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public void setIdJefeLineaEjecutor(Long idJefeLineaEjecutor) {
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
    }

    public void setIdCreadaPor(Long idCreadaPor) {
        this.idCreadaPor = idCreadaPor;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setEstado(EstadoOrdenProduccion estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFechaInicioReal(LocalDateTime fechaInicioReal) {
        this.fechaInicioReal = fechaInicioReal;
    }

    public void setFechaFinReal(LocalDateTime fechaFinReal) {
        this.fechaFinReal = fechaFinReal;
    }
}