package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;

public class SaldoTanqueLeche {

    private Long idTanque;
    private String nombre;
    private TipoTanqueLeche tipo;
    private BigDecimal saldoLitros;
    private Boolean activo;

    public SaldoTanqueLeche(Long idTanque, String nombre, TipoTanqueLeche tipo,
            BigDecimal saldoLitros, Boolean activo) {
        this.idTanque = idTanque;
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldoLitros = saldoLitros;
        this.activo = activo;
    }

    public Long getIdTanque() {
        return idTanque;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoTanqueLeche getTipo() {
        return tipo;
    }

    public BigDecimal getSaldoLitros() {
        return saldoLitros;
    }

    public Boolean getActivo() {
        return activo;
    }
}
