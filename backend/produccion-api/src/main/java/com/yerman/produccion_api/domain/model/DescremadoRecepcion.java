package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class DescremadoRecepcion {

    private Long id;
    private Long idRecepcionLeche;
    private Long idTanqueDestino;
    private BigDecimal litrosDescremados;
    private BigDecimal cremaObtenidaKg;
    private Long idSkuCrema;
    private Integer unidadesCrema;
    private BigDecimal kgPorUnidadCrema;
    private String loteCrema;
    private Long idMovimientoSalida;
    private Long idMovimientoEntrada;
    private String observaciones;

    public DescremadoRecepcion() {
    }

    public DescremadoRecepcion(Long id, Long idRecepcionLeche, Long idTanqueDestino, BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg, Long idSkuCrema, Integer unidadesCrema, BigDecimal kgPorUnidadCrema,
            String loteCrema, Long idMovimientoSalida, Long idMovimientoEntrada, String observaciones) {
        this.id = id;
        this.idRecepcionLeche = idRecepcionLeche;
        this.idTanqueDestino = idTanqueDestino;
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

    public Long getIdTanqueDestino() {
        return idTanqueDestino;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdRecepcionLeche(Long idRecepcionLeche) {
        this.idRecepcionLeche = idRecepcionLeche;
    }

    public void setIdTanqueDestino(Long idTanqueDestino) {
        this.idTanqueDestino = idTanqueDestino;
    }

    public void setLitrosDescremados(BigDecimal litrosDescremados) {
        this.litrosDescremados = litrosDescremados;
    }

    public void setCremaObtenidaKg(BigDecimal cremaObtenidaKg) {
        this.cremaObtenidaKg = cremaObtenidaKg;
    }

    public void setIdSkuCrema(Long idSkuCrema) {
        this.idSkuCrema = idSkuCrema;
    }

    public void setUnidadesCrema(Integer unidadesCrema) {
        this.unidadesCrema = unidadesCrema;
    }

    public void setKgPorUnidadCrema(BigDecimal kgPorUnidadCrema) {
        this.kgPorUnidadCrema = kgPorUnidadCrema;
    }

    public void setLoteCrema(String loteCrema) {
        this.loteCrema = loteCrema;
    }

    public void setIdMovimientoSalida(Long idMovimientoSalida) {
        this.idMovimientoSalida = idMovimientoSalida;
    }

    public void setIdMovimientoEntrada(Long idMovimientoEntrada) {
        this.idMovimientoEntrada = idMovimientoEntrada;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
