package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProgramacionProduccionRequest {

    @NotNull(message = "La fecha de producción es obligatoria")
    @FutureOrPresent(message = "La fecha de producción no puede ser anterior a hoy")
    private LocalDate fechaProduccion;

    @NotNull(message = "La línea es obligatoria")
    @Positive(message = "El id de la línea debe ser mayor que cero")
    private Long idLinea;

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El id del producto debe ser mayor que cero")
    private Long idProducto;

    @NotNull(message = "El turno es obligatorio")
    @Positive(message = "El id del turno debe ser mayor que cero")
    private Long idTurno;

    @PositiveOrZero(message = "El número de baches planeados no puede ser negativo")
    private Integer numBachesPlan;

    @PositiveOrZero(message = "Los kg por bache no pueden ser negativos")
    private BigDecimal kgBachePlan;

    @NotNull(message = "La versión de fórmula es obligatoria")
    @Positive(message = "El id de la versión de fórmula debe ser mayor que cero")
    private Long idFormulaVersion;

    @NotNull(message = "El jefe de producción es obligatorio")
    @Positive(message = "El id del jefe de producción debe ser mayor que cero")
    private Long idJefeProduccion;

    @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres")
    private String observaciones;

    public LocalDate getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(LocalDate fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public Integer getNumBachesPlan() {
        return numBachesPlan;
    }

    public void setNumBachesPlan(Integer numBachesPlan) {
        this.numBachesPlan = numBachesPlan;
    }

    public BigDecimal getKgBachePlan() {
        return kgBachePlan;
    }

    public void setKgBachePlan(BigDecimal kgBachePlan) {
        this.kgBachePlan = kgBachePlan;
    }

    public Long getIdFormulaVersion() {
        return idFormulaVersion;
    }

    public void setIdFormulaVersion(Long idFormulaVersion) {
        this.idFormulaVersion = idFormulaVersion;
    }

    public Long getIdJefeProduccion() {
        return idJefeProduccion;
    }

    public void setIdJefeProduccion(Long idJefeProduccion) {
        this.idJefeProduccion = idJefeProduccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}