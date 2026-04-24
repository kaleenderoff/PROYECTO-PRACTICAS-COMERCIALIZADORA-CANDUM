package com.yerman.produccion_api.application.dto.response;

public class ProgramacionSkuResponse {

    private Long id;
    private Long idProgramacion;
    private Long idSku;
    private Integer unidadesObjetivo;
    private String observaciones;

    public ProgramacionSkuResponse(Long id, Long idProgramacion, Long idSku, Integer unidadesObjetivo,
            String observaciones) {
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
}