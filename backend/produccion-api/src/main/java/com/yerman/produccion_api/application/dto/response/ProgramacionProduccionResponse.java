package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProgramacionProduccionResponse {

    private Long id;
    private String codigoProgramacion;
    private LocalDate fechaProduccion;
    private Long idLinea;
    private Long idProducto;
    private Long idTurno;
    private Integer numBachesPlan;
    private BigDecimal kgBachePlan;
    private Long idFormulaVersion;
    private Long idJefeProduccion;
    private String estado;
    private String observaciones;

    public ProgramacionProduccionResponse(Long id, String codigoProgramacion, LocalDate fechaProduccion,
            Long idLinea, Long idProducto, Long idTurno,
            Integer numBachesPlan, BigDecimal kgBachePlan,
            Long idFormulaVersion, Long idJefeProduccion,
            String estado, String observaciones) {
        this.id = id;
        this.codigoProgramacion = codigoProgramacion;
        this.fechaProduccion = fechaProduccion;
        this.idLinea = idLinea;
        this.idProducto = idProducto;
        this.idTurno = idTurno;
        this.numBachesPlan = numBachesPlan;
        this.kgBachePlan = kgBachePlan;
        this.idFormulaVersion = idFormulaVersion;
        this.idJefeProduccion = idJefeProduccion;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoProgramacion() {
        return codigoProgramacion;
    }

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
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

    public Integer getNumBachesPlan() {
        return numBachesPlan;
    }

    public BigDecimal getKgBachePlan() {
        return kgBachePlan;
    }

    public Long getIdFormulaVersion() {
        return idFormulaVersion;
    }

    public Long getIdJefeProduccion() {
        return idJefeProduccion;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }
}