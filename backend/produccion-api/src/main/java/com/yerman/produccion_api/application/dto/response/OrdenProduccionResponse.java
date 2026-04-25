package com.yerman.produccion_api.application.dto.response;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrdenProduccionResponse {

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

    public OrdenProduccionResponse() {
    }

    public OrdenProduccionResponse(Long id, String numeroOrden, Long idProgramacion, Long idLinea, Long idProducto,
            Long idTurno, Long idJefeLineaEjecutor, Long idCreadaPor, LocalDate fechaProduccion,
            EstadoOrdenProduccion estado, String observaciones, LocalDateTime fechaInicioReal,
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
}