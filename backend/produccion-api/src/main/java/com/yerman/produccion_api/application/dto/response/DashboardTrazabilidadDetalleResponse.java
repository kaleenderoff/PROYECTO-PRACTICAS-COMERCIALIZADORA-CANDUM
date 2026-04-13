package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardTrazabilidadDetalleResponse {

    private Long idDetalleProduccion;
    private Long idProducto;
    private String nombreProducto;
    private Integer numBatch;
    private BigDecimal kgProgramados;
    private BigDecimal kgBatch;
    private Integer unidadesReales;
    private BigDecimal rendimientoPct;
    private String estadoValidacion;
    private LocalDateTime fechaValidacion;
    private String observacionValidacion;
    private List<DashboardTrazabilidadEmpaqueResponse> empaques;

    public DashboardTrazabilidadDetalleResponse() {
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getNumBatch() {
        return numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public BigDecimal getKgProgramados() {
        return kgProgramados;
    }

    public void setKgProgramados(BigDecimal kgProgramados) {
        this.kgProgramados = kgProgramados;
    }

    public BigDecimal getKgBatch() {
        return kgBatch;
    }

    public void setKgBatch(BigDecimal kgBatch) {
        this.kgBatch = kgBatch;
    }

    public Integer getUnidadesReales() {
        return unidadesReales;
    }

    public void setUnidadesReales(Integer unidadesReales) {
        this.unidadesReales = unidadesReales;
    }

    public BigDecimal getRendimientoPct() {
        return rendimientoPct;
    }

    public void setRendimientoPct(BigDecimal rendimientoPct) {
        this.rendimientoPct = rendimientoPct;
    }

    public String getEstadoValidacion() {
        return estadoValidacion;
    }

    public void setEstadoValidacion(String estadoValidacion) {
        this.estadoValidacion = estadoValidacion;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public String getObservacionValidacion() {
        return observacionValidacion;
    }

    public void setObservacionValidacion(String observacionValidacion) {
        this.observacionValidacion = observacionValidacion;
    }

    public List<DashboardTrazabilidadEmpaqueResponse> getEmpaques() {
        return empaques;
    }

    public void setEmpaques(List<DashboardTrazabilidadEmpaqueResponse> empaques) {
        this.empaques = empaques;
    }
}