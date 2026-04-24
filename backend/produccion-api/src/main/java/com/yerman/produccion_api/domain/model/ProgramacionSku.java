package com.yerman.produccion_api.domain.model;

public class ProgramacionSku {

    private Long id;
    private Long idProgramacion;
    private Long idSku;
    private Integer unidadesObjetivo;
    private String observaciones;

    public ProgramacionSku() {
    }

    public ProgramacionSku(Long id, Long idProgramacion, Long idSku, Integer unidadesObjetivo, String observaciones) {
        this.id = id;
        this.idProgramacion = idProgramacion;
        this.idSku = idSku;
        this.unidadesObjetivo = unidadesObjetivo;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProgramacion() {
        return idProgramacion;
    }

    public Long getIdSku() {
        return idSku;
    }

    public Integer getUnidadesObjetivo() {
        return unidadesObjetivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProgramacion(Long idProgramacion) {
        this.idProgramacion = idProgramacion;
    }

    public void setIdSku(Long idSku) {
        this.idSku = idSku;
    }

    public void setUnidadesObjetivo(Integer unidadesObjetivo) {
        this.unidadesObjetivo = unidadesObjetivo;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}