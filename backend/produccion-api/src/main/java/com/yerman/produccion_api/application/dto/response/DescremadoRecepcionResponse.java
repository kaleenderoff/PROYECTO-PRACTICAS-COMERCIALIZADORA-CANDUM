package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;

public class DescremadoRecepcionResponse {

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

    public DescremadoRecepcionResponse(Long id, Long idRecepcionLeche, Long idTanqueDestino,
            BigDecimal litrosDescremados, BigDecimal cremaObtenidaKg,
            Long idSkuCrema, Integer unidadesCrema, BigDecimal kgPorUnidadCrema, String loteCrema,
            Long idMovimientoSalida, Long idMovimientoEntrada,
            String observaciones) {
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
}
