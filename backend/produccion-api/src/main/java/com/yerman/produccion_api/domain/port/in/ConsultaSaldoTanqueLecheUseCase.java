package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;

import java.util.List;

public interface ConsultaSaldoTanqueLecheUseCase {

    List<SaldoTanqueLeche> listarSaldos();

    SaldoTanqueLeche obtenerSaldo(Long idTanque);
}
