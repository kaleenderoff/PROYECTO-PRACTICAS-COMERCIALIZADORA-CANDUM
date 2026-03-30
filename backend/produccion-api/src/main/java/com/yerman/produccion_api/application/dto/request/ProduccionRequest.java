package com.yerman.produccion_api.application.dto.request;

import java.time.LocalDate;

public class ProduccionRequest {

    private LocalDate fechaProduccion;
    private String tipoTurno;
    private String numeroLote;
    private String estado;
    private String observacionesGenerales;

    private Long idLineaProduccion;
    private Long idOperario;
    private Long idJefeLinea;

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public Long getIdLineaProduccion() {
        return idLineaProduccion;
    }

    public void setIdLineaProduccion(Long idLineaProduccion) {
        this.idLineaProduccion = idLineaProduccion;
    }

    public Long getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(Long idOperario) {
        this.idOperario = idOperario;
    }

    public Long getIdJefeLinea() {
        return idJefeLinea;
    }

    public void setIdJefeLinea(Long idJefeLinea) {
        this.idJefeLinea = idJefeLinea;
    }
}
