package com.yerman.produccion_api.application.dto.response;

import com.yerman.produccion_api.domain.model.TipoTanqueLeche;

import java.math.BigDecimal;

public class SaldoTanqueLecheResponse {

    private final Long idTanque;
    private final String nombre;
    private final TipoTanqueLeche tipo;
    private final BigDecimal saldoLitros;
    private final Boolean activo;

    public SaldoTanqueLecheResponse(Long idTanque, String nombre, TipoTanqueLeche tipo,
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
