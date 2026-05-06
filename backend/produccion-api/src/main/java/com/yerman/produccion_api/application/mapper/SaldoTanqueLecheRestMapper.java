package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.SaldoTanqueLecheResponse;
import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;

public class SaldoTanqueLecheRestMapper {

    private SaldoTanqueLecheRestMapper() {
    }

    public static SaldoTanqueLecheResponse toResponse(SaldoTanqueLeche saldo) {
        if (saldo == null) {
            return null;
        }

        return new SaldoTanqueLecheResponse(
                saldo.getIdTanque(),
                saldo.getNombre(),
                saldo.getTipo(),
                saldo.getSaldoLitros(),
                saldo.getActivo());
    }
}
