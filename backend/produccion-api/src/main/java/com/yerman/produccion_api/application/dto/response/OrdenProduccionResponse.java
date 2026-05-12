package com.yerman.produccion_api.application.dto.response;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrdenProduccionResponse {

    private Long id;
    private String numeroOrden;

    private Long idProgramacion;

    private Long idLinea;
    private String nombreLinea;

    private Long idProducto;
    private String nombreProducto;

    private Long idTurno;
    private String nombreTurno;

    private Long idJefeLineaEjecutor;
    private String nombreJefeLineaEjecutor;

    private Long idCreadaPor;
    private String nombreCreadaPor;

    private LocalDate fechaProduccion;
    private EstadoOrdenProduccion estado;
    private String observaciones;
    private LocalDateTime fechaInicioReal;
    private LocalDateTime fechaFinReal;

    public OrdenProduccionResponse() {
    }

    public OrdenProduccionResponse(
            Long id,
            String numeroOrden,
            Long idProgramacion,
            Long idLinea,
            String nombreLinea,
            Long idProducto,
            String nombreProducto,
            Long idTurno,
            String nombreTurno,
            Long idJefeLineaEjecutor,
            String nombreJefeLineaEjecutor,
            Long idCreadaPor,
            String nombreCreadaPor,
            LocalDate fechaProduccion,
            EstadoOrdenProduccion estado,
            String observaciones,
            LocalDateTime fechaInicioReal,
            LocalDateTime fechaFinReal) {

        this.id = id;
        this.numeroOrden = numeroOrden;
        this.idProgramacion = idProgramacion;
        this.idLinea = idLinea;
        this.nombreLinea = nombreLinea;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.idTurno = idTurno;
        this.nombreTurno = nombreTurno;
        this.idJefeLineaEjecutor = idJefeLineaEjecutor;
        this.nombreJefeLineaEjecutor = nombreJefeLineaEjecutor;
        this.idCreadaPor = idCreadaPor;
        this.nombreCreadaPor = nombreCreadaPor;
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

    public String getNombreLinea() {
        return nombreLinea;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public Long getIdTurno() {
        return idTurno;
    }

    public String getNombreTurno() {
        return nombreTurno;
    }

    public Long getIdJefeLineaEjecutor() {
        return idJefeLineaEjecutor;
    }

    public String getNombreJefeLineaEjecutor() {
        return nombreJefeLineaEjecutor;
    }

    public Long getIdCreadaPor() {
        return idCreadaPor;
    }

    public String getNombreCreadaPor() {
        return nombreCreadaPor;
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
}