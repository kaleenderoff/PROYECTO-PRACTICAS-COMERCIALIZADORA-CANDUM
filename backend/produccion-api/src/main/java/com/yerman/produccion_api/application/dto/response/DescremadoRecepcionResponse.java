package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DescremadoRecepcionResponse {

    private Long id;
    private Long idRecepcionLeche;
    private LocalDate fechaDescremado;
    private Long idTanqueOrigen;
    private Long idTanqueDestino;
    private Long idUsuario;
    private BigDecimal litrosDescremados;
    private BigDecimal cremaObtenidaKg;
    private Long idSkuCrema;
    private Integer unidadesCrema;
    private BigDecimal kgPorUnidadCrema;
    private String loteCrema;
    private Long idMovimientoSalida;
    private Long idMovimientoEntrada;
    private String observaciones;

    public DescremadoRecepcionResponse(
            Long id,
            Long idRecepcionLeche,
            LocalDate fechaDescremado,
            Long idTanqueOrigen,
            Long idTanqueDestino,
            Long idUsuario,
            BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg,
            Long idSkuCrema,
            Integer unidadesCrema,
            BigDecimal kgPorUnidadCrema,
            String loteCrema,
            Long idMovimientoSalida,
            Long idMovimientoEntrada,
            String observaciones) {
        this.id = id;
        this.idRecepcionLeche = idRecepcionLeche;
        this.fechaDescremado = fechaDescremado;
        this.idTanqueOrigen = idTanqueOrigen;
        this.idTanqueDestino = idTanqueDestino;
        this.idUsuario = idUsuario;
        this.litrosDescremados = litrosDescremados;
        this.cremaObtenidaKg = cremaObtenidaKg;
        this.idSkuCrema = idSkuCrema;
        this.unidadesCrema = unidadesCrema;
        this.kgPorUnidadCrema = kgPorUnidadCrema;
        this.loteCrema = loteCrema;
        this.idMovimientoSalida = idMovimientoSalida;
        this.idMovimientoEntrada = idMovimientoEntrada;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getIdRecepcionLeche() {
        return idRecepcionLeche;
    }

    public LocalDate getFechaDescremado() {
        return fechaDescremado;
    }

    public Long getIdTanqueOrigen() {
        return idTanqueOrigen;
    }

    public Long getIdTanqueDestino() {
        return idTanqueDestino;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public BigDecimal getLitrosDescremados() {
        return litrosDescremados;
    }

    public BigDecimal getCremaObtenidaKg() {
        return cremaObtenidaKg;
    }

    public Long getIdSkuCrema() {
        return idSkuCrema;
    }

    public Integer getUnidadesCrema() {
        return unidadesCrema;
    }

    public BigDecimal getKgPorUnidadCrema() {
        return kgPorUnidadCrema;
    }

    public String getLoteCrema() {
        return loteCrema;
    }

    public Long getIdMovimientoSalida() {
        return idMovimientoSalida;
    }

    public Long getIdMovimientoEntrada() {
        return idMovimientoEntrada;
    }

    public String getObservaciones() {
        return observaciones;
    }
}